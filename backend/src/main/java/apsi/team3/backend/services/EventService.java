package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.Requests.CreateEventRequest;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IEventService;
import apsi.team3.backend.model.EventEntity;
import apsi.team3.backend.model.UserEntity;
import apsi.team3.backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService implements IEventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) { 
        this.eventRepository = eventRepository;
    }

    @Override
    public Optional<EventEntity> getEventById(Long id) { 
        return eventRepository.findEventById(id);
    }

    @Override
    public List<EventEntity> getAllEvents() { 
        return eventRepository.findAll();
    }

    @Override
    // TODO: dodać nawet prostą walidację
    // TODO: przetestować zachowanie dat, nie wiem czy dobrze się ustawiają, strefy czasowe itd 
    public void save(CreateEventRequest request) throws ApsiValidationException{
        if (request == null || request.name == null || request.name.isBlank())
            throw new ApsiValidationException("Należy podać nazwę wydarzenia", "name");
        
        var loggedUser = (UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var entity = new EventEntity(request, loggedUser.getId());
        this.eventRepository.save(entity);
    }
}
