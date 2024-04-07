package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.EventDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IEventService;
import apsi.team3.backend.model.UserEntity;
import apsi.team3.backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService implements IEventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Optional<EventDTO> getEventById(Long id) {
        return eventRepository.findById(id).map(DTOMapper::toDTO);
    }

    @Override
    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    // TODO: dodać nawet prostą walidację
    // TODO: przetestować zachowanie dat, nie wiem czy dobrze się ustawiają, strefy czasowe itd 
    public EventDTO create(EventDTO eventDTO) throws ApsiValidationException {
        if (eventDTO.getId() != null)
            throw new ApsiValidationException("Id must be null", "id");

        validate(eventDTO);

        var loggedUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var entity = DTOMapper.toEntity(eventDTO);
        entity.setOrganizerId(loggedUser.getId());
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

    private static void validate(EventDTO eventDTO) throws ApsiValidationException {
        if (eventDTO == null || eventDTO.getName() == null || eventDTO.getName().isBlank())
            throw new ApsiValidationException("Należy podać nazwę wydarzenia", "name");
    }


}
