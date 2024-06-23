package apsi.team3.backend.controller;

import apsi.team3.backend.DTOs.EventDTO;
import apsi.team3.backend.DTOs.ImageDTO;
import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.helpers.QRCodeGenerator;
import apsi.team3.backend.interfaces.IEventService;

import apsi.team3.backend.model.MailStructure;
import apsi.team3.backend.model.User;
import apsi.team3.backend.services.MailService;
import apsi.team3.backend.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/events")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class EventController {
    private final IEventService eventService;
    private final static int MAX_IMAGE_SIZE = 500_000;
    private final MailService mailService;
    private final TicketService ticketService;

    @Autowired
    public EventController(IEventService eventService, MailService mailService, TicketService ticketService) {
        this.eventService = eventService;
        this.mailService = mailService;
        this.ticketService = ticketService;
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
    public ResponseEntity<List<ImageDTO>> getEventImages(@PathVariable("id") Long eventId) {
        var images = eventService.getImagesByEventId(eventId);
        return ResponseEntity.ok(images);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<EventDTO> createEvent(@RequestParam(name = "image", required = false) MultipartFile image,
        @RequestParam(name = "sectionMap", required = false) MultipartFile sectionMap,
        @RequestParam("event") String event
    ) throws ApsiValidationException
    {
        if (image != null && image.getSize() > MAX_IMAGE_SIZE)
            throw new ApsiValidationException("Zbyt duży obraz. Maksymalna wielkość to 500 KB", "image");
        if (sectionMap != null && sectionMap.getSize() > MAX_IMAGE_SIZE)
            throw new ApsiValidationException("Zbyt duży obraz. Maksymalna wielkość to 500 KB", "sectionMap");

        try {
            var mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            var dto = mapper.readValue(event, EventDTO.class);
            var resp = eventService.create(dto, image, sectionMap);
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
        @RequestPart(name = "image", required = false) MultipartFile image,
        @RequestPart(name = "sectionMap", required = false) MultipartFile sectionMap
    ) throws ApsiValidationException
    {
        if (image != null && image.getSize() > MAX_IMAGE_SIZE)
            throw new ApsiValidationException("Zbyt duży obraz. Maksymalna wielkość to 500 KB", "image");
        if (sectionMap != null && sectionMap.getSize() > MAX_IMAGE_SIZE)
            throw new ApsiValidationException("Zbyt duży obraz. Maksymalna wielkość to 500 KB", "sectionMap");

        EventDTO eventDTO;
        try {
            var mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            eventDTO = mapper.readValue(event, EventDTO.class);
        } catch (JsonProcessingException e) {
            throw new ApsiValidationException("Niepoprawne żądanie", "id");
        }

        validateSameId(id, eventDTO);
        var oldEvent = eventService.getEventById(id);
        if (oldEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resp = eventService.replace(eventDTO, image, sectionMap);

        boolean timeChanged = !Objects.equals(eventDTO.getStartTime(), oldEvent.get().getStartTime())
            || !Objects.equals(eventDTO.getEndTime(), oldEvent.get().getEndTime())
            || !Objects.equals(eventDTO.getStartDate(), oldEvent.get().getStartDate())
            || !Objects.equals(eventDTO.getEndDate(), oldEvent.get().getEndDate());

        boolean locationChanged = eventDTO.getLocation().getId() != oldEvent.get().getLocation().getId();
        var sections = resp.getSections();

        if (timeChanged || locationChanged) {
            try {
                List<TicketDTO> tickets = ticketService.getTicketsByEventId(oldEvent.get().getId());
                for (TicketDTO ticket : tickets) {
                    ticket.setEvent(resp);
                    var QRCode = QRCodeGenerator.generateQRCodeByte(ticket.toJSON());
                    ticket.setQRCode(QRCodeGenerator.convertQRCodeByteToBase64(QRCode));

                    var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    var section = sections.stream().filter(s -> s.getId().equals(ticket.getSectionId())).findFirst();
                    var ticketData = mailService.getTicketContentParams(ticket, section.get().getName());
                    var mailSubject = "Szczegóły wydarzenia, w którym uczestniczysz uległy zmianie";

                    var mailStructure = new MailStructure(mailSubject, QRCode, ticketData);
                    mailService.sendTicketMail(user.getEmail(), mailStructure);
                }
            } catch (Exception ignored) {
                // we hope everyone gets an email but failing update when some got email and some didn't doesn't seem right
            }
        }

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
