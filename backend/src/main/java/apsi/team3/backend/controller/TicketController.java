package apsi.team3.backend.controller;

import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/tickets")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class TicketController {
    private final ITicketService ticketService;

    @Autowired TicketController(ITicketService ticketService) { this.ticketService = ticketService; }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable("id") Long id) {
        Optional<TicketDTO> ticket = ticketService.getTicketById(id);
        return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TicketDTO> createTicketType(@RequestBody TicketDTO ticketDTO) throws ApsiValidationException {
        var resp = ticketService.create(ticketDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
