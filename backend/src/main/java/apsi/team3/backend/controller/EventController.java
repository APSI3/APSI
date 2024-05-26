package apsi.team3.backend.controller;

import apsi.team3.backend.DTOs.EventDTO;
import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IEventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.util.Base64;
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
    public ResponseEntity<PaginatedList<EventDTO>> getEvents(
        @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDate from, 
        @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDate to,
        @RequestParam int pageIndex
    ) throws ApsiValidationException {
        var allEvents = eventService.getEvents(from, to, pageIndex); 
        return ResponseEntity.ok(allEvents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable("id") Long id) {
        Optional<EventDTO> event = eventService.getEventById(id);

        return event.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> getEventImage(@PathVariable("id") Long eventId) {
        var image = eventService.getImageByEventId(eventId);
        var base64encodedData = Base64.getEncoder().encode(image);
        return ResponseEntity.ok(base64encodedData);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<EventDTO> createEvent(@RequestParam(name = "image", required = false) MultipartFile image, @RequestParam("event") String event) throws ApsiValidationException {
        if (image != null && image.getSize() > 500_000)
            throw new ApsiValidationException("Zbyt duży obraz. Maksymalna wielkość to 500 KB", "image");

        try {
            var mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            var dto = mapper.readValue(event, EventDTO.class);
            var resp = eventService.create(dto, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        }
        catch (JsonProcessingException e){
            throw new ApsiValidationException("Niepoprawne żądanie", "id");
        }
    }

    @PutMapping(value="/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<EventDTO> replaceEvent(
            @PathVariable("id") Long id,
            @RequestPart("event") String event,
            @RequestPart(name = "image", required = false) MultipartFile image
    ) throws ApsiValidationException {
        try {
            var mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            var eventDTO = mapper.readValue(event, EventDTO.class);

            validateSameId(id, eventDTO);
            if (eventService.notExists(id)) {
                return ResponseEntity.notFound().build();
            }
            var resp = eventService.replace(eventDTO, image);
            return ResponseEntity.ok(resp);
        } catch (JsonProcessingException e){
            throw new ApsiValidationException("Niepoprawne żądanie", "id");
        }
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
