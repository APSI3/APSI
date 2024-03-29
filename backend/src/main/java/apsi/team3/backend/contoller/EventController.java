package apsi.team3.backend.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apsi.team3.backend.DTOs.ApiResponse;
import apsi.team3.backend.DTOs.Requests.CreateEventRequest;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IEventService;

@RestController
@RequestMapping("/event")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class EventController {
    private final IEventService eventService;

    @Autowired
    public EventController(IEventService eventService) { this.eventService = eventService; }

    // TODO: odkomentować przy implementowaniu strony eventu i listy
    // @GetMapping
    // public List<EventEntity> getAllEvents() { 
    //     return eventService.getAllEvents();
    // }

    // @GetMapping("/{id}")
    // public Optional<EventEntity> getEventById(@PathVariable("id") Long id) { 
    //     return eventService.getEventById(id);
    // }

    @PostMapping("/create")
    public ApiResponse<Boolean> createEvent(@RequestBody CreateEventRequest request) throws ApsiValidationException {
        eventService.save(request);
        return new ApiResponse<Boolean>(true);
    }
}