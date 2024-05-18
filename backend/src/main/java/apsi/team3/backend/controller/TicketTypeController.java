package apsi.team3.backend.controller;

import apsi.team3.backend.DTOs.TicketTypeDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.ITicketTypeService;
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

    @Autowired
    public TicketTypeController(ITicketTypeService ticketTypeService) { this.ticketTypeService = ticketTypeService; }

    @GetMapping("/{id}")
    public ResponseEntity<TicketTypeDTO> getTicketTypeById(@PathVariable("id") Long id) {
        Optional<TicketTypeDTO> ticketType = ticketTypeService.getTicketTypeById(id);
        return ticketType.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/count")
    public ResponseEntity<Long> getTicketCountByTypeId(@PathVariable("id") Long id) {
        Optional<Long> ticketCount = ticketTypeService.getTicketCountByTypeId(id);
        return ticketCount.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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

    @PutMapping("/{id}")
    public ResponseEntity<TicketTypeDTO> replaceTicketType(@PathVariable("id") Long id, @RequestBody TicketTypeDTO ticketTypeDTO) throws ApsiValidationException {
        if (ticketTypeService.notExists(id)) {
            return ResponseEntity.notFound().build();
        }
        var resp = ticketTypeService.replace(ticketTypeDTO);
        return ResponseEntity.ok(resp);
    }
}
