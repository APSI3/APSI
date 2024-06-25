package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.EventDTO;
import apsi.team3.backend.DTOs.ImageDTO;
import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.DTOs.TicketTypeDTO;
import apsi.team3.backend.exceptions.ApsiException;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IEventService;
import apsi.team3.backend.model.Event;
import apsi.team3.backend.model.EventImage;
import apsi.team3.backend.model.User;
import apsi.team3.backend.repository.EventImageRepository;
import apsi.team3.backend.repository.EventRepository;
import apsi.team3.backend.repository.EventSectionRepository;
import apsi.team3.backend.repository.LocationRepository;
import apsi.team3.backend.repository.TicketRepository;
import apsi.team3.backend.repository.TicketTypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private final EventSectionRepository eventSectionRepository;
    private final TicketRepository ticketRepository;
    private final TicketService ticketService;
    private final MailService mailService;

    @Autowired
    public EventService(
        EventRepository eventRepository,
        LocationRepository locationRepository,
        TicketTypeRepository ticketTypeRepository,
        EventImageRepository eventImageRepository,
        EventSectionRepository eventSectionRepository,
        TicketRepository ticketRepository,
        TicketService ticketService,
        MailService mailService
    ) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.eventImageRepository = eventImageRepository;
        this.eventSectionRepository = eventSectionRepository;
        this.ticketRepository = ticketRepository;
        this.ticketService = ticketService;
        this.mailService = mailService;
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
            if (eventDTO.getLocation().getId() == 0){
                eventDTO.setLocation(null);
            }
            else {
                var location = locationRepository.findById(eventDTO.getLocation().getId());
                if (location.isEmpty())
                    throw new ApsiValidationException("Wybrana lokalizacja nie istnieje", "location");

                if (eventDTO.getTicketTypes().size() > 0 &&
                    location.get().getCapacity() != 0 &&
                    location.get().getCapacity() < eventDTO.getTicketTypes().stream().mapToInt(TicketTypeDTO::getQuantityAvailable).sum()
                )
                    throw new ApsiValidationException("Ilość biletów większa niż dopuszczalna w danej lokalizacji", "tickets");

                if (!Objects.equals(location.get().getCreator().getId(), loggedUser.getId()))
                    throw new ApsiValidationException("Lokalizacja niedostępna", "location");
            }
        }

        if (eventDTO.getTicketTypes().size() < 1)
            throw new ApsiValidationException("Należy stworzyć przynajmniej jeden typ biletów", "tickets");
        if (eventDTO.getTicketTypes().size() > 16)
            throw new ApsiValidationException("Można stworzyć maksymalnie 16 typów biletów", "tickets");
        if (!eventDTO.getTicketTypes().stream().allMatch(x -> x.getName() != null && !x.getName().isEmpty() && x.getName().length() < 100))
            throw new ApsiValidationException("Dla każdego typu biletów należy podać maksymalnie 100-znakową nazwę", "tickets");
        if (eventDTO.getSections().size() < 1)
            throw new ApsiValidationException("Należy stworzyć przynajmniej jedną sekcję z miejscami", "sections");
        if (!eventDTO.getSections().stream().allMatch(x -> x.getName() != null && !x.getName().isEmpty() && x.getName().length() < 64))
            throw new ApsiValidationException("Nazwa sekcji nie może być dłuższa niż 64 znaki", "sections");
        if (!eventDTO.getSections().stream().allMatch(x -> x.getCapacity() >= 1))
            throw new ApsiValidationException("Sekcja musi mieć przynajmniej jedno dostępne miejsce", "sections");

        var ttSum = eventDTO.getTicketTypes().stream().mapToInt(tt -> tt.getQuantityAvailable()).sum();
        var sectionSum = eventDTO.getSections().stream().mapToInt(tt -> tt.getCapacity()).sum();
        if (ttSum > sectionSum)
            throw new ApsiValidationException("Nie można sprzedać więcej biletów niż dostępnych miejsc", "sections");
    }

    @Override
    public Optional<EventDTO> getEventById(Long id) {
        var event = eventRepository.findById(id);
        Optional<EventDTO> dto = event.map(e -> DTOMapper.toDTO(e));

        if (event.isPresent() && dto.isPresent() && event.get().getSections().size() > 0){
            var ticketsPerSection = ticketRepository.countTicketsBySectionForEvent(event.get().getId());
            var dict = ticketsPerSection.stream().collect(Collectors.toMap(a -> a.section_id, b -> b.count));
            var sectionDtos = event.get().getSections().stream().map(s -> DTOMapper.toDTO(s, dict.getOrDefault(s.getId(), 0l))).toList();
            dto.get().setSections(sectionDtos);
        }

        return dto;
    }

    @Override
    public PaginatedList<EventDTO> getEvents(LocalDate from, LocalDate to, int pageIndex) throws ApsiValidationException {
        validatePaginationArgs(from, to, pageIndex);

        var page = eventRepository.getEventsWithDatesBetween(PageRequest.of(pageIndex, PAGE_SIZE), from, to);

        var items = page
            .stream()
            .filter(e -> !e.isCanceled())
            .map(DTOMapper::toDTO)
            .collect(Collectors.toList());

        return new PaginatedList<>(items, pageIndex, page.getTotalElements(), page.getTotalPages());
    }

    @Override
    public PaginatedList<EventDTO> getOrganizerEvents(LocalDate from, LocalDate to, int pageIndex) throws ApsiValidationException {
        validatePaginationArgs(from, to, pageIndex);
        var loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var page = eventRepository.getEventsWithDatesBetweenForOrganizer(PageRequest.of(pageIndex, PAGE_SIZE), from, to, loggedUser.getId());

        var items = page
            .stream()
            .map(DTOMapper::toDTO)
            .collect(Collectors.toList());

        return new PaginatedList<>(items, pageIndex, page.getTotalElements(), page.getTotalPages());
    }

    @Override
    public EventDTO replace(EventDTO eventDTO, MultipartFile image, MultipartFile sectionMap) throws ApsiValidationException {
        if (eventDTO.getId() == null)
            throw new ApsiValidationException("Identyfikator wydarzenia jest wymagany", "id");

        var existingEvent = eventRepository.findById(eventDTO.getId())
            .orElseThrow(() -> new ApsiValidationException("Wydarzenie nie zostało znalezione", "id"));
        var existingImageId = existingEvent.getImages().stream().filter(i -> !i.isSection_map()).map(i -> i.getId()).findFirst();
        var existingSectionMap = existingEvent.getImages().stream().filter(i -> i.isSection_map()).map(i -> i.getId()).findFirst();

        var loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        validateEvent(eventDTO, loggedUser);

        var entity = DTOMapper.toEntity(eventDTO);
        entity = prepareEventEntity(entity, loggedUser);
        entity.setId(existingEvent.getId());  // Ensure the ID is retained
        entity.setImages(existingEvent.getImages()); // Retain existing images list

        processRelatedEntities(eventDTO, entity, existingEvent);
        processEventImage(image, entity);
        processSectionMap(sectionMap, entity);

        if (image != null && existingImageId.isPresent()){
            existingEvent.getImages().removeIf(i -> i.getId() == existingImageId.get());
            entity.getImages().removeIf(i -> i.getId() == existingImageId.get());
            eventImageRepository.deleteById(existingImageId.get());
        }

        if (sectionMap != null && existingSectionMap.isPresent()) {
            existingEvent.getImages().removeIf(i -> i.getId() == existingSectionMap.get());
            entity.getImages().removeIf(i -> i.getId() == existingSectionMap.get());
            eventImageRepository.deleteById(existingSectionMap.get());
        }

        var updatedEvent = eventRepository.save(entity);

        return DTOMapper.toDTO(updatedEvent);
    }

    private void processEventImage(MultipartFile image, Event updatedEvent) throws ApsiValidationException {
        if (image == null) return;
        byte[] bytes;
        try {
            bytes = image.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApsiValidationException("Uszkodzony plik obrazu", "image");
        }

        var eventImage = EventImage.builder()
            .image(bytes)
            .event(updatedEvent)
            .section_map(false)
            .build();
        eventImageRepository.save(eventImage);

        updatedEvent.getImages().add(eventImage);
    }

    private void processSectionMap(MultipartFile sectionMap, Event updatedEvent) throws ApsiValidationException {
        if (sectionMap == null)
            return;
        byte[] bytes;
        try {
            bytes = sectionMap.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApsiValidationException("Uszkodzony plik obrazu", "sectionMap");
        }

        var eventImage = EventImage.builder()
            .image(bytes)
            .event(updatedEvent)
            .section_map(true)
            .build();
        eventImageRepository.save(eventImage);

        updatedEvent.getImages().add(eventImage);
    }

    @Override
    public EventDTO create(EventDTO eventDTO, MultipartFile image, MultipartFile sectionMap) throws ApsiValidationException {
        if (eventDTO.getId() != null){
            throw new ApsiValidationException("Podano niedozwolony identyfikator wydarzenia", "id");
        }

        var loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        validateEvent(eventDTO, loggedUser);

        var entity = DTOMapper.toEntityWithNullCollections(eventDTO);
        entity = prepareEventEntity(entity, loggedUser);

        var savedEvent = eventRepository.save(entity);
        savedEvent.setImages(new ArrayList<>());

        if (!eventDTO.getTicketTypes().isEmpty()) {
            var entities = eventDTO.getTicketTypes().stream()
                .map(e -> DTOMapper.toEntity(e, savedEvent))
                .toList();
            var savedTickets = ticketTypeRepository.saveAll(entities);
            savedEvent.setTicketTypes(savedTickets);
        }

        if (!eventDTO.getSections().isEmpty()) {
            var entities = eventDTO.getSections().stream().map(e -> DTOMapper.toEntity(e, savedEvent)).toList();
            var sections = eventSectionRepository.saveAll(entities);
            savedEvent.setSections(sections);
        }

        processEventImage(image, savedEvent);
        processSectionMap(sectionMap, savedEvent);

        return DTOMapper.toDTO(savedEvent);
    }

    private Event prepareEventEntity(Event entity, User loggedUser) throws ApsiValidationException {
        entity.setOrganizer(loggedUser);

        if (entity.getLocation() != null) {
            var loc = locationRepository.findById(entity.getLocation().getId()).get();
            entity.setLocation(loc);
        }

        return entity;
    }

    private void processRelatedEntities(EventDTO eventDTO, Event newEvent, Event oldEvent) throws ApsiValidationException {
        List<Long> newTTIds = eventDTO.getTicketTypes().stream().map(tt -> tt.getId()).toList();
        var ticketTypesToDelete = oldEvent.getTicketTypes().stream().filter(s -> !newTTIds.contains(s.getId())).toList();

        for (var tt : ticketTypesToDelete) {
            var tickets = ticketService.getTicketsByTicketTypeId(tt.getId());
            ticketService.deleteByTicketTypeId(tt.getId());
            ticketTypeRepository.deleteById(tt.getId());
    
            for (var ticket : tickets) {
                try {
                    mailService.sendEmailTicketTypeDeleted(ticket);
                }
                catch (Exception e) { }
            }
        }

        for (var tt : eventDTO.getTicketTypes()){
            if (tt.getId() == null) continue;

            var count = ticketTypeRepository.findTicketCount(tt.getId());
            if (tt.getQuantityAvailable() < count){
                throw new ApsiValidationException("Nie można zmniejszyć liczby dostępnych biletów poniżej kupionej liczby",
                    "ticketTypes");
            }
        }

        var newTTs = eventDTO.getTicketTypes().stream().map(e -> DTOMapper.toEntity(e, newEvent)).toList();
        var savedTicketTypes = ticketTypeRepository.saveAll(newTTs);
        newEvent.setTicketTypes(savedTicketTypes);

        List<Long> newSectionIds = eventDTO.getSections().stream().map(s -> s.getId()).toList();
        var sectionToDelete = oldEvent.getSections().stream().filter(s -> !newSectionIds.contains(s.getId())).toList();

        for (var s : sectionToDelete) {
            var tickets = Arrays.stream(ticketRepository.getBySectionId(s.getId())).map(DTOMapper::toDTO).toList();
            ticketRepository.deleteAllById(tickets.stream().map(t -> t.getId()).toList());
            eventSectionRepository.deleteById(s.getId());

            for (var ticket : tickets) {
                try {
                    mailService.sendEmailSectionDeleted(ticket);
                }
                catch (Exception e) { }
            }
        }

        for (var s : eventDTO.getSections()) {
            if (s.getId() == null) continue;
            var count = ticketRepository.countTicketsForSectionId(s.getId());
            if (s.getCapacity() < count) {
                throw new ApsiValidationException(
                    "Nie można zmniejszyć liczby dostępnych biletów poniżej kupionej liczby",
                    "sections");
            }
        }

        var newSections = eventDTO.getSections().stream().map(e -> DTOMapper.toEntity(e, newEvent)).toList();
        var savedSections = eventSectionRepository.saveAll(newSections);
        newEvent.setSections(savedSections);
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
    public List<ImageDTO> getImagesByEventId(Long id) {
        var images = eventImageRepository.findByEventId(id);
        return images.stream().map(i -> DTOMapper.toDTO(i)).toList();
    }

    @Override
    public Optional<EventDTO> cancel(Long id) throws ApsiException {
        var eventOptional = eventRepository.findById(id);
        if (eventOptional.isEmpty()) {
            return Optional.empty();
        }
        var event = eventOptional.get();
        event.setCanceled(true);
        eventRepository.save(event);
        var tickets = ticketRepository.getTicketsByEventId(event.getId());
        for (var ticket : tickets) {
            mailService.sendEventDeletedEmail(DTOMapper.toDTO(ticket));
        }
        return Optional.of(DTOMapper.toDTO(event));
    }
}
