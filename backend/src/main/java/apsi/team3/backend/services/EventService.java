package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.EventDTO;
import apsi.team3.backend.DTOs.ImageDTO;
import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IEventService;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService implements IEventService {
    private final int PAGE_SIZE = 10;
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final EventImageRepository eventImageRepository;
    private final EventSectionRepository eventSectionRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public EventService(
        EventRepository eventRepository,
        LocationRepository locationRepository,
        TicketTypeRepository ticketTypeRepository,
        EventImageRepository eventImageRepository,
        EventSectionRepository eventSectionRepository,
        TicketRepository ticketRepository
    ) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.eventImageRepository = eventImageRepository;
        this.eventSectionRepository = eventSectionRepository;
        this.ticketRepository = ticketRepository;
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
                location.get().getCapacity() < eventDTO.getTicketTypes().stream().mapToInt(e -> e.getQuantityAvailable()).sum()
            )
                throw new ApsiValidationException("Ilość biletów większa niż dopuszczalna w danej lokalizacji", "tickets");

            if (location.get().getCreator().getId() != loggedUser.getId())
                throw new ApsiValidationException("Lokalizacja niedostępna", "location");
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
            var sectionDtos = event.get().getSections().stream().map(s -> DTOMapper.toDTO(s, dict.getOrDefault(s.getId(), 0))).toList();
            dto.get().setSections(sectionDtos);
        }

        return dto;
    }

    @Override
    public PaginatedList<EventDTO> getEvents(LocalDate from, LocalDate to, int pageIndex) throws ApsiValidationException {
        if (from == null)
            throw new ApsiValidationException("Należy podać datę początkową", "from");
        if (to == null)
            throw new ApsiValidationException("Należy podać datę końcową", "to");
        if (from.isAfter(to))
            throw new ApsiValidationException("Data końcowa nie może być mniejsza niż początkowa", "to");
        if (pageIndex < 0)
            throw new ApsiValidationException("Indeks strony nie może być ujemny", "pageIndex");

        var page = eventRepository.getEventsWithDatesBetween(PageRequest.of(pageIndex, PAGE_SIZE), from, to);

        var items = page
            .stream()
            .map(DTOMapper::toDTO)
            .collect(Collectors.toList());

        return new PaginatedList<EventDTO>(items, pageIndex, page.getTotalElements(), page.getTotalPages());
    }

    @Override
    public EventDTO create(EventDTO eventDTO, MultipartFile image, MultipartFile sectionMap) throws ApsiValidationException {
        if (eventDTO.getId() != null)
            throw new ApsiValidationException("Podano niedozwolony identyfikator wydarzenia", "id");

        var loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        validateEvent(eventDTO, loggedUser);
        
        var entity = DTOMapper.toEntity(eventDTO);
        entity.setOrganizer(loggedUser);

        if (entity.getLocation() != null){
            var loc = locationRepository.findById(entity.getLocation().getId()).get();
            entity.setLocation(loc);
        }

        var imgBytes = imgToBytes(image, "image");
        var sectionMapBytes = imgToBytes(sectionMap, "sectionMap");

        var saved = eventRepository.save(entity);
        saved.setImages(new ArrayList<>());
        
        if (!eventDTO.getTicketTypes().isEmpty()){
            var entities = eventDTO.getTicketTypes().stream().map(e -> DTOMapper.toEntity(e, saved)).toList();
            var savedTickets = ticketTypeRepository.saveAll(entities);
            saved.setTicketTypes(savedTickets);
        }

        if (!eventDTO.getSections().isEmpty()) {
            var entities = eventDTO.getSections().stream().map(e -> DTOMapper.toEntity(e, saved)).toList();
            var sections = eventSectionRepository.saveAll(entities);
            saved.setSections(sections);
        }

        if (imgBytes != null) {
            var eventImage = EventImage.builder()
                .image(imgBytes)
                .event(saved)
                .section_map(false)
                .build();
            eventImageRepository.save(eventImage);
            saved.getImages().add(eventImage);
        }

        if (sectionMapBytes != null) {
            var eventImage = EventImage.builder()
                .image(sectionMapBytes)
                .event(saved)
                .section_map(true)
                .build();
            eventImageRepository.save(eventImage);
            saved.getImages().add(eventImage);
        }

        return DTOMapper.toDTO(saved);
    }

    private byte[] imgToBytes(MultipartFile image, String key) throws ApsiValidationException {
        if (image == null)
            return null;

        try {
            return image.getBytes();
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new ApsiValidationException("Uszkodzony plik obrazu", key);
        }
    }

    @Override
    public EventDTO replace(EventDTO eventDTO) throws ApsiValidationException {
        var loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        validateEvent(eventDTO, loggedUser);

        var entity = DTOMapper.toEntity(eventDTO);
        var saved = eventRepository.save(entity);

        return DTOMapper.toDTO(saved);
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
}
