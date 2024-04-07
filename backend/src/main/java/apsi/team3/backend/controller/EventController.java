package apsi.team3.backend.controller;

import apsi.team3.backend.DTOs.EventDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public ResponseEntity<CollectionModel<EventDTO>> getAllEvents() {
        var allEvents = eventService.getAllEvents().stream().map(EventController::addSelfLink).toList();

        Link selfLink = linkTo(methodOn(EventController.class).getAllEvents()).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(allEvents, selfLink));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable("id") Long id) {
        Optional<EventDTO> event = eventService.getEventById(id);
        return event.map(EventController::addSelfLink).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) throws ApsiValidationException {
        var resp = eventService.create(eventDTO);
        var withLink = addSelfLink(resp);
        return ResponseEntity.status(HttpStatus.CREATED).body(withLink);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> replaceEvent(@PathVariable("id") Long id, @RequestBody EventDTO eventDTO) throws ApsiValidationException {
        validateSameId(id, eventDTO);
        if (eventService.notExists(id)) {
            return ResponseEntity.notFound().build();
        }
        var resp = eventService.replace(eventDTO);
        var withLink = addSelfLink(resp);
        return ResponseEntity.ok(withLink);
    }

//    TODO: Patch trochę skomplikowany i może być niepotrzebny
//    @PatchMapping("/books/{id}")
//    public ResponseEntity<EventDTO> updateEvent(@PathVariable("id") Long id, @RequestBody EventDTO eventDTO) throws ApsiValidationException {
//
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable("id") Long id) {
        if (eventService.notExists(id)) {
            return ResponseEntity.notFound().build();
        }
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private static EventDTO addSelfLink(EventDTO e) {
        Link selfLink = linkTo(methodOn(EventController.class).getEventById(e.getId())).withSelfRel();
        e.add(selfLink);
        return e;
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
