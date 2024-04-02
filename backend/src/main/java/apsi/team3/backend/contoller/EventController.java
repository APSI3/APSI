package apsi.team3.backend.contoller;

import apsi.team3.backend.DTOs.Responses.GetEventsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import apsi.team3.backend.DTOs.ApiResponse;
import apsi.team3.backend.DTOs.Requests.CreateEventRequest;
import apsi.team3.backend.DTOs.Responses.CreateEventResponse;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IEventService;

@RestController
@RequestMapping("/event")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class EventController {
    private final IEventService eventService;

    @Autowired
    public EventController(IEventService eventService) { this.eventService = eventService; }

     @PostMapping("/all")
     public ApiResponse<GetEventsResponse> getAllEvents() {
         var resp = eventService.getAllEvents();
         return new ApiResponse<>(resp);
     }

    // TODO: odkomentować przy implementowaniu strony eventu
    // @GetMapping("/{id}")
    // public Optional<EventEntity> getEventById(@PathVariable("id") Long id) { 
    //     return eventService.getEventById(id);
    // }

    @PostMapping("/create")
    public ApiResponse<CreateEventResponse> createEvent(@RequestBody CreateEventRequest request) throws ApsiValidationException {
        var resp = eventService.save(request);
        return new ApiResponse<>(resp);
    }
}