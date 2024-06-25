package apsi.team3.backend.controller;

import apsi.team3.backend.DTOs.Requests.CreateTicketRequest;
import apsi.team3.backend.DTOs.*;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.helpers.QRCodeGenerator;
import apsi.team3.backend.interfaces.ITicketService;
import apsi.team3.backend.model.User;
import jakarta.mail.MessagingException;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
import java.time.LocalDate;

@RestController
@RequestMapping("/tickets")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class TicketController {
    private final ITicketService ticketService;

    @Autowired TicketController(ITicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable("id") Long id) {
        Optional<TicketDTO> ticket = ticketService.getTicketById(id);
        ticket.ifPresent(t -> {
            try {
                t.setQRCode(QRCodeGenerator.generateQRCodeBase64(t.toJSON()));
            } catch (WriterException | IOException e) {
                t.setQRCode(null);
            }
        });
        return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<PaginatedList<TicketDTO>> getTicketsByUserId(
        @PathVariable("id") Long id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate from,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate to,
        @RequestParam int pageIndex
    ) throws ApsiValidationException
    {
        var tickets = ticketService.getTicketsByUserId(id, from, to, pageIndex);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<PaginatedList<TicketDTO>> getMyTickets(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate from,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate to,
        @RequestParam int pageIndex
    ) throws ApsiValidationException
    {
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var tickets = ticketService.getTicketsByUserId(user.getId(), from, to, pageIndex);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody CreateTicketRequest request) throws ApsiValidationException, IOException, WriterException, MessagingException {
        var resp = ticketService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
