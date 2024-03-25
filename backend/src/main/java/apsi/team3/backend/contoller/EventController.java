package apsi.team3.backend.contoller;

import apsi.team3.backend.interfaces.IEventService;
import apsi.team3.backend.model.EventEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.util.List;

@RestController
@RequestMapping("/event")
@CrossOrigin(origins = {"http://localhost:3000"})
public class EventController {
    private final IEventService eventService;

    @Autowired
    public EventController(IEventService eventService) { this.eventService = eventService; }

    @GetMapping
    public List<EventEntity> getAllEvents() { return eventService.getAllEvents(); }

    @GetMapping("/{id}")
    public EventEntity getEventById(@PathVariable("id") Long id) { return eventService.getEventById(id); }

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<EventEntity> createEvent(@RequestBody EventEntity newEvent) throws ServerException {
        EventEntity event = eventService.save(newEvent);
        if (event == null) {
            throw new ServerException("Encountered error saving event");
        } else {
            return new ResponseEntity<>(event, HttpStatus.CREATED);
        }
    }
}