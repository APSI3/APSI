package apsi.team3.backend.controller;

import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.helpers.QRCodeGenerator;
import apsi.team3.backend.interfaces.IEventService;
import apsi.team3.backend.interfaces.ITicketService;
import apsi.team3.backend.interfaces.ITicketTypeService;
import apsi.team3.backend.interfaces.IUserService;
import apsi.team3.backend.model.MailStructure;
import apsi.team3.backend.services.MailService;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final IUserService userService;
    private final IEventService eventService;

    @Autowired TicketController(ITicketService ticketService, ITicketTypeService ticketTypeService, MailService mailService, IUserService userService, IEventService eventService) {
        this.ticketService = ticketService;
        this.ticketTypeService = ticketTypeService;
        this.mailService = mailService;
        this.userService = userService;
        this.eventService = eventService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable("id") Long id) {
        Optional<TicketDTO> ticket = ticketService.getTicketById(id);
        return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDTO) throws ApsiValidationException, IOException, WriterException, MessagingException {
        var resp = ticketService.create(ticketDTO);
        var user = userService.getUserById(ticketDTO.getHolderId());
        var ticketType = ticketTypeService.getTicketTypeById(ticketDTO.getTicketTypeId());
        var event = eventService.getEventById(ticketType.get().getEventId());

        var QRCode = QRCodeGenerator.generateQRCode(ticketDTO.toString());
        resp.setQRCode(QRCode);

        String mailSubject = "Twój bilet jest tutaj!";
        Map<String, String> ticketData =  Map.of(
                "eventName", event.get().getName(),
                "date", getDateString(event.get().getStartDate(), event.get().getEndDate()),
                "ticketType", ticketType.get().getName(),
                "price", ticketType.get().getPrice().toString(),
                "holderName", user.get().getLogin()
        );
        MailStructure mailStructure = new MailStructure(
                mailSubject,
                resp.getQRCode(),
                QRCode,
                ticketData
        );
        mailService.sendMail(user.get().getEmail(), mailStructure);

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
