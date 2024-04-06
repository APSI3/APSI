package apsi.team3.backend.controller;

import apsi.team3.backend.DTOs.EventDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class EventController {
    private final IEventService eventService;

    @Autowired
    public EventController(IEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        var allEvents = eventService.getAllEvents();
        return ResponseEntity.ok(allEvents);
    }

    // TODO: odkomentować przy implementowaniu strony eventu
    // @GetMapping("/{id}")
    // public Optional<EventEntity> getEventById(@PathVariable("id") Long id) { 
    //     return eventService.getEventById(id);
    // }

    @PostMapping("/create")
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) throws ApsiValidationException {
        var resp = eventService.save(eventDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}