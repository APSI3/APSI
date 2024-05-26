package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.EventDTO;
import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.DTOs.TicketTypeDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IEventService;
import apsi.team3.backend.model.Event;
import apsi.team3.backend.model.EventImage;
import apsi.team3.backend.model.User;
import apsi.team3.backend.repository.EventImageRepository;
import apsi.team3.backend.repository.EventRepository;
import apsi.team3.backend.repository.LocationRepository;
import apsi.team3.backend.repository.TicketTypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static apsi.team3.backend.helpers.PaginationValidator.validatePaginationArgs;

@Service
public class EventService implements IEventService {
    private final int PAGE_SIZE = 10;
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final EventImageRepository eventImageRepository;

    @Autowired
    public EventService(
        EventRepository eventRepository,
        LocationRepository locationRepository,
        TicketTypeRepository ticketTypeRepository,
        EventImageRepository eventImageRepository
    ) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.eventImageRepository = eventImageRepository;
    }

    private void validateEvent(EventDTO eventDTO, User loggedUser) throws ApsiValidationException {
        if (eventDTO == null || eventDTO.getName() == null || eventDTO.getName().isBlank())
            throw new ApsiValidationException("Należy podać nazwę wydarzenia", "name");
        if (eventDTO.getName().length() > 255)
            throw new ApsiValidationException("Zbyt długa nazwa wydarzenia", "name");
        if (eventDTO.getDescription().length() > 2000)
            throw new ApsiValidationException("Zbyt długi opis wydarzenia", "description");
        if (eventDTO.getStartDate().isAfter(eventDTO.getEndDate()))
            throw new ApsiValidationException("Data końcowa nie może być wcześniejsza niż data początkowa", "endDate");
        if (eventDTO.getEndDate().isBefore(LocalDate.now()))
            throw new ApsiValidationException("Data końcowa nie może być przeszła", "endDate");
        if (eventDTO.getStartTime() != null && eventDTO.getEndTime() != null)
            if (eventDTO.getStartDate().atTime(eventDTO.getStartTime()).isAfter(eventDTO.getEndDate().atTime(eventDTO.getEndTime())))
                throw new ApsiValidationException("Data początkowa nie może być wcześniejsza niż data końcowa", "startDate");

        if (eventDTO.getLocation() != null && eventDTO.getLocation().getId() != null) {
            var location = locationRepository.findById(eventDTO.getLocation().getId());
            if (location.isEmpty())
                throw new ApsiValidationException("Wybrana lokacja nie istnieje", "location");

            if (eventDTO.getTicketTypes().size() > 0 && 
                location.get().getCapacity() != 0 && 
                location.get().getCapacity() < eventDTO.getTicketTypes().stream().mapToInt(TicketTypeDTO::getQuantityAvailable).sum()
            )
                throw new ApsiValidationException("Ilość biletów większa niż dopuszczalna w danej lokalizacji", "tickets");

            if (!Objects.equals(location.get().getCreator().getId(), loggedUser.getId()))
                throw new ApsiValidationException("Lokalizacja niedostępna", "location");
        }

        if (eventDTO.getTicketTypes().size() < 1)
            throw new ApsiValidationException("Należy stworzyć przynajmniej jeden typ biletów", "tickets");
        if (eventDTO.getTicketTypes().size() > 16)
            throw new ApsiValidationException("Można stworzyć maksymalnie 16 typów biletów", "tickets");
        if (!eventDTO.getTicketTypes().stream().allMatch(x -> x.getName() != null && !x.getName().isEmpty() && x.getName().length() < 100))
            throw new ApsiValidationException("Dla każdego typu biletów należy podać maksymalnie 100-znakową nazwę", "tickets");
    }

    @Override
    public Optional<EventDTO> getEventById(Long id) {
        return eventRepository.findById(id).map(DTOMapper::toDTO);
    }

    @Override
    public PaginatedList<EventDTO> getEvents(LocalDate from, LocalDate to, int pageIndex) throws ApsiValidationException {
        validatePaginationArgs(from, to, pageIndex);

        var page = eventRepository.getEventsWithDatesBetween(PageRequest.of(pageIndex, PAGE_SIZE), from, to);

        var items = page
            .stream()
            .map(DTOMapper::toDTO)
            .collect(Collectors.toList());

        return new PaginatedList<>(items, pageIndex, page.getTotalElements(), page.getTotalPages());
    }

    @Override
    public EventDTO replace(EventDTO eventDTO, MultipartFile image) throws ApsiValidationException {
        if (eventDTO.getId() == null) {
            throw new ApsiValidationException("Identyfikator wydarzenia jest wymagany", "id");
        }

        var existingEvent = eventRepository.findById(eventDTO.getId())
                .orElseThrow(() -> new ApsiValidationException("Wydarzenie nie zostało znalezione", "id"));

        var loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        validateEvent(eventDTO, loggedUser);

        var entity = prepareEventEntity(eventDTO, loggedUser);
        entity.setId(existingEvent.getId());  // Ensure the ID is retained
        entity.setImages(existingEvent.getImages()); // Retain existing images list

        processTicketTypes(eventDTO, entity);
        processEventImage(image, entity);

        var updatedEvent = eventRepository.save(entity);

        return DTOMapper.toDTO(updatedEvent);
    }

    private void processEventImage(MultipartFile image, Event updatedEvent) throws ApsiValidationException {
        if (image != null) {
            byte[] bytes;
            try {
                bytes = image.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
                throw new ApsiValidationException("Uszkodzony plik obrazu", "image");
            }

            // Clear existing images if any
            updatedEvent.getImages().clear();

            // Save new image
            var eventImage = EventImage.builder()
                    .image(bytes)
                    .event(updatedEvent)
                    .build();
            eventImageRepository.save(eventImage);

            updatedEvent.getImages().add(eventImage);
        }
    }

    @Override
    public EventDTO create(EventDTO eventDTO, MultipartFile image) throws ApsiValidationException {
        if (eventDTO.getId() != null) {
            throw new ApsiValidationException("Podano niedozwolony identyfikator wydarzenia", "id");
        }

        var loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        validateEvent(eventDTO, loggedUser);

        var entity = prepareEventEntity(eventDTO, loggedUser);

        var savedEvent = eventRepository.save(entity);

        processTicketTypes(eventDTO, savedEvent);
        processEventImage(image, savedEvent);

        return DTOMapper.toDTO(savedEvent);
    }

    private void validateImage(MultipartFile image) throws ApsiValidationException {
        if (image != null && image.getSize() > 500_000) {
            throw new ApsiValidationException("Zbyt duży obraz. Maksymalna wielkość to 500 KB", "image");
        }
    }

    private Event prepareEventEntity(EventDTO eventDTO, User loggedUser) {
        var entity = DTOMapper.toEntity(eventDTO);
        entity.setOrganizer(loggedUser);

        if (entity.getLocation() != null) {
            var loc = locationRepository.findById(entity.getLocation().getId()).get();
            entity.setLocation(loc);
        }

        return entity;
    }

    private void processTicketTypes(EventDTO eventDTO, Event updatedEvent) {
        if (!eventDTO.getTicketTypes().isEmpty()) {
            var entities = eventDTO.getTicketTypes().stream()
                    .map(e -> DTOMapper.toEntity(e, updatedEvent))
                    .toList();
            var savedTickets = ticketTypeRepository.saveAll(entities);
            updatedEvent.setTicketTypes(savedTickets);
        }
    }

    @Override
    public void delete(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public boolean notExists(Long id) {
        return !eventRepository.existsById(id);
    }

    @Override
    public byte[] getImageByEventId(Long id) {
        var images = eventImageRepository.findByEventId(id);

        if (images.size() == 0)
            return new byte[0];
        
        // na razie spodziewamy się 1 obrazka per event 
        return images.get(0).getImage();
    }
}
