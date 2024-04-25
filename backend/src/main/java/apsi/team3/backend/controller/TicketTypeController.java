package apsi.team3.backend.controller;

import apsi.team3.backend.DTOs.TicketTypeDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.ITicketTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        return ticketType.map(TicketTypeController::addSelfLink).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/count")
    public ResponseEntity<Map<String, Object>> getTicketCountByTypeId(@PathVariable("id") Long id) {
        Optional<Long> ticketCount = ticketTypeService.getTicketCountByTypeId(id);
        Map<String, Object> response = new HashMap<>();
        response.put("_links", Collections.singletonMap("self", Collections.singletonMap("href", ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString())));
        ticketCount.ifPresent(count -> response.put("_embedded", Collections.singletonMap("count", count)));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<CollectionModel<TicketTypeDTO>> getTicketTypeByEventId(@PathVariable("id") Long eventId) {
        var eventTicketTypes = ticketTypeService.getTicketTypeByEventId(eventId);
        Link selfLink = linkTo(methodOn(TicketTypeController.class).getTicketTypeByEventId(eventId)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(eventTicketTypes, selfLink));
    }

    @PostMapping
    public ResponseEntity<TicketTypeDTO> createTicketType(@RequestBody TicketTypeDTO ticketTypeDTO) throws ApsiValidationException {
        var resp = ticketTypeService.create(ticketTypeDTO);
        var withLink = addSelfLink(resp);
        return ResponseEntity.status(HttpStatus.CREATED).body(withLink);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketTypeDTO> replaceTicketType(@PathVariable("id") Long id, @RequestBody TicketTypeDTO ticketTypeDTO) throws ApsiValidationException {
        if (ticketTypeService.notExists(id)) {
            return ResponseEntity.notFound().build();
        }
        var resp = ticketTypeService.replace(ticketTypeDTO);
        var withLink = addSelfLink(resp);
        return ResponseEntity.ok(withLink);
    }

    private static TicketTypeDTO addSelfLink(TicketTypeDTO t) {
        Link selfLink = linkTo(methodOn(TicketTypeController.class).getTicketTypeById(t.getId())).withSelfRel();
        t.add(selfLink);
        return t;
    }
}
