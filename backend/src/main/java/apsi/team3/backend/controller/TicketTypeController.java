package apsi.team3.backend.controller;

import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.DTOs.TicketTypeDTO;
import apsi.team3.backend.exceptions.ApsiException;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.ITicketService;
import apsi.team3.backend.interfaces.ITicketTypeService;
import apsi.team3.backend.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/ticket_types")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class TicketTypeController {
    private final ITicketTypeService ticketTypeService;
    private final ITicketService ticketService;
    private final MailService mailService;

    @Autowired
    public TicketTypeController(ITicketTypeService ticketTypeService, ITicketService ticketService, MailService mailService) {
        this.ticketTypeService = ticketTypeService;
        this.ticketService = ticketService;
        this.mailService = mailService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketTypeDTO> getTicketTypeById(@PathVariable("id") Long id) {
        Optional<TicketTypeDTO> ticketType = ticketTypeService.getTicketTypeById(id);
        return ticketType.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/count")
    public ResponseEntity<Long> getTicketCountByTypeId(@PathVariable("id") Long id) {
        var ticketCount = ticketTypeService.getTicketCountByTypeId(id);
        return ResponseEntity.ok(ticketCount);
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<List<TicketTypeDTO>> getTicketTypeByEventId(@PathVariable("id") Long eventId) {
        var eventTicketTypes = ticketTypeService.getTicketTypesByEventId(eventId);
        return ResponseEntity.ok(eventTicketTypes);
    }

    @PostMapping
    public ResponseEntity<TicketTypeDTO> createTicketType(@RequestBody TicketTypeDTO ticketTypeDTO) throws ApsiValidationException {
        var resp = ticketTypeService.create(ticketTypeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicketType(@PathVariable("id") Long id) throws ApsiException {
        var ticketType = ticketTypeService.getTicketTypeById(id);
        if (ticketType.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var tickets = ticketService.getTicketsByTicketTypeId(id);
        ticketService.deleteByTicketTypeId(id);
        ticketTypeService.delete(id);

        for (TicketDTO ticket : tickets) {
            mailService.sendEmailTicketTypeDeleted(ticket);
        }
        return ResponseEntity.noContent().build();
    }
}
