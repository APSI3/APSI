package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.EventDTO;
import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IEventService;
import apsi.team3.backend.model.User;
import apsi.team3.backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService implements IEventService {
    private final int PAGE_SIZE = 10;
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    private static void validate(EventDTO eventDTO) throws ApsiValidationException {
        if (eventDTO == null || eventDTO.getName() == null || eventDTO.getName().isBlank())
            throw new ApsiValidationException("Należy podać nazwę wydarzenia", "name");
    }

    @Override
    public Optional<EventDTO> getEventById(Long id) {
        return eventRepository.findById(id).map(DTOMapper::toDTO);
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
    // TODO: dodać nawet prostą walidację
    // TODO: przetestować zachowanie dat, nie wiem czy dobrze się ustawiają, strefy czasowe itd
    public EventDTO create(EventDTO eventDTO) throws ApsiValidationException {
        if (eventDTO.getId() != null)
            throw new ApsiValidationException("Podano niedozwolony identyfikator wydarzenia", "id");

        validate(eventDTO);

        var loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var entity = DTOMapper.toEntity(eventDTO);
        entity.setOrganizer(loggedUser);
        var saved = eventRepository.save(entity);

        return DTOMapper.toDTO(saved);
    }

    @Override
    public EventDTO replace(EventDTO eventDTO) throws ApsiValidationException {
        validate(eventDTO);

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
}
