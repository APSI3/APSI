package apsi.team3.backend.controller;

import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.helpers.QRCodeGenerator;
import apsi.team3.backend.interfaces.ITicketService;
import apsi.team3.backend.model.MailStructure;
import apsi.team3.backend.model.User;
import apsi.team3.backend.services.MailService;
import apsi.team3.backend.services.UserService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/tickets")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class TicketController {
    private final ITicketService ticketService;
    private final MailService mailService;
    private final UserService userService;

    @Autowired TicketController(ITicketService ticketService, MailService mailService, UserService userService) {
        this.ticketService = ticketService;
        this.mailService = mailService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable("id") Long id) {
        Optional<TicketDTO> ticket = ticketService.getTicketById(id);
        return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // todo 3: display QR code inside mail message

    @PostMapping
    public ResponseEntity<TicketDTO> createTicketType(@RequestBody TicketDTO ticketDTO) throws ApsiValidationException, IOException, WriterException {
        var resp = ticketService.create(ticketDTO);
        var user = userService.getUserById(ticketDTO.getHolderId());
        resp.setQRCode(QRCodeGenerator.generateQRCode(ticketDTO.toString()));

        String mailSubject = "Your ticket is here!";
        MailStructure mailStructure = new MailStructure(
                mailSubject,
                resp.getQRCode()
        );
        mailService.sendMail(user.get().getEmail(), mailStructure);

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
