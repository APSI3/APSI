package apsi.team3.backend.controller;

import apsi.team3.backend.DTOs.*;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.helpers.QRCodeGenerator;
import apsi.team3.backend.interfaces.IEventService;
import apsi.team3.backend.interfaces.ITicketService;
import apsi.team3.backend.interfaces.ITicketTypeService;
import apsi.team3.backend.model.User;
import apsi.team3.backend.services.MailService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static apsi.team3.backend.helpers.MailGenerator.getDateString;
import static apsi.team3.backend.helpers.MailSender.sendTicketByEmail;

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
                t.setQRCode(QRCodeGenerator.generateQRCode(t.toJSON(null)));
            } catch (WriterException | IOException e) {
                t.setQRCode(null);
            }
        });
        return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("user/{id}/extended")
    public ResponseEntity<PaginatedList<TicketDTO>> getExtendedTicketsByUserId(
            @PathVariable("id") Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate to,
            @RequestParam int pageIndex
    ) throws ApsiValidationException {
        var tickets = ticketService.getTicketsByUserId(id, from, to, pageIndex);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDTO) throws ApsiValidationException {
        if (ticketDTO.getTicketType() == null)
            throw new ApsiValidationException("Niepoprawny typ biletu", "ticketTypeId");
        
        var sold = ticketTypeService.getTicketCountByTypeId(ticketDTO.getTicketType().getId());
        if (sold.get() > ticketDTO.getTicketType().getQuantityAvailable())
            throw new ApsiValidationException("Sprzedaż biletu niemożliwa. Bilety tego typu wyprzedane", "ticketTypeId");

        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (ticketDTO.getHolder() == null)
            ticketDTO.setHolder(DTOMapper.toDTO(user));

        if (!Objects.equals(ticketDTO.getHolder().getId(), user.getId()))
            throw new ApsiValidationException("Nie można kupić biletu na konto innego użytkownika", "holderId");

        var resp = ticketService.create(ticketDTO);

        sendTicketByEmail(mailService, "Twój bilet jest tutaj!", resp);

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
