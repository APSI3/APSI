package apsi.team3.backend.controller;

import apsi.team3.backend.DTOs.EventDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class EventController {
    private final IEventService eventService;

    @Autowired
    public EventController(IEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        var allEvents = eventService.getAllEvents().stream().toList();
        return ResponseEntity.ok(allEvents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable("id") Long id) {
        Optional<EventDTO> event = eventService.getEventById(id);

        if (!event.isPresent())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(event.get());
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) throws ApsiValidationException {
        var resp = eventService.create(eventDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> replaceEvent(@PathVariable("id") Long id, @RequestBody EventDTO eventDTO) throws ApsiValidationException {
        validateSameId(id, eventDTO);
        if (eventService.notExists(id)) {
            return ResponseEntity.notFound().build();
        }
        var resp = eventService.replace(eventDTO);
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable("id") Long id) {
        if (eventService.notExists(id)) {
            return ResponseEntity.notFound().build();
        }
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private static void validateSameId(Long id, EventDTO eventDTO) throws ApsiValidationException {
        if (eventDTO.getId() == null) {
            throw new ApsiValidationException("Id must not be null", "id");
        }
        if (!id.equals(eventDTO.getId())) {
            throw new ApsiValidationException("Id in path and body must be the same", "id");
        }
    }
}
