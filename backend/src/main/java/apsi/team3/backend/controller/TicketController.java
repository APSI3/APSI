package apsi.team3.backend.controller;

import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.helpers.QRCodeGenerator;
import apsi.team3.backend.interfaces.IEventService;
import apsi.team3.backend.interfaces.ITicketService;
import apsi.team3.backend.interfaces.ITicketTypeService;
import apsi.team3.backend.model.MailStructure;
import apsi.team3.backend.model.User;
import apsi.team3.backend.services.MailService;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static apsi.team3.backend.helpers.MailGenerator.getDateString;

@RestController
@RequestMapping("/tickets")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class TicketController {
    private final ITicketService ticketService;
    private final ITicketTypeService ticketTypeService;
    private final MailService mailService;
    private final IEventService eventService;

    @Autowired TicketController(ITicketService ticketService, ITicketTypeService ticketTypeService, MailService mailService, IEventService eventService) {
        this.ticketService = ticketService;
        this.ticketTypeService = ticketTypeService;
        this.mailService = mailService;
        this.eventService = eventService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable("id") Long id) {
        Optional<TicketDTO> ticket = ticketService.getTicketById(id);
        ticket.ifPresent(t -> {
            try {
                t.setQRCode(QRCodeGenerator.generateQRCode(t.toString()));
            } catch (WriterException | IOException e) {
                t.setQRCode(null);
            }
        });
        return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDTO) throws ApsiValidationException {
        var ticketType = ticketTypeService.getTicketTypeById(ticketDTO.getTicketTypeId());
        if (!ticketType.isPresent())
            throw new ApsiValidationException("Niepoprawny typ biletu", "ticketTypeId");
        
        var sold = ticketTypeService.getTicketCountByTypeId(ticketDTO.getTicketTypeId());
        if (sold.get() >= ticketType.get().getQuantityAvailable())
            throw new ApsiValidationException("Sprzedaż biletu niemożliwa. Bilety tego typu wyprzedane", "ticketTypeId");

        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (ticketDTO.getHolderId() == null)
            ticketDTO.setHolderId(user.getId());

        if (ticketDTO.getHolderId() != user.getId())
            throw new ApsiValidationException("Nie można kupić biletu na konto innego użytkownika", "holderId");

        var event = eventService.getEventById(ticketType.get().getEventId());
        var resp = ticketService.create(ticketDTO);
        resp.setEventId(event.get().getId());

        String QRCode;
        try {
            QRCode = QRCodeGenerator.generateQRCode(resp.toString());
            resp.setQRCode(QRCode);
        }
        catch (WriterException|IOException e) {
            throw new ApsiValidationException(e);
        }

        String mailSubject = "Twój bilet jest tutaj!";
        Map<String, String> ticketData =  Map.of(
                "eventName", event.get().getName(),
                "date", getDateString(event.get().getStartDate(), event.get().getEndDate()),
                "ticketType", ticketType.get().getName(),
                "price", ticketType.get().getPrice().toString(),
                "holderName", user.getLogin()
        );
        MailStructure mailStructure = new MailStructure(
                mailSubject,
                resp.getQRCode(),
                QRCode,
                ticketData
        );
        try {
            mailService.sendMail(user.getEmail(), mailStructure);
        } catch (MessagingException e) {
            throw new ApsiValidationException("Nie udało się wysłać maila z zakupionym biletem", "mail");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
