package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.EventDTO;
import apsi.team3.backend.DTOs.Requests.CreateEventRequest;
import apsi.team3.backend.DTOs.Responses.CreateEventResponse;
import apsi.team3.backend.DTOs.Responses.GetEventsResponse;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IEventService;
import apsi.team3.backend.model.EventEntity;
import apsi.team3.backend.model.UserEntity;
import apsi.team3.backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    public Optional<EventEntity> getEventById(Long id) { 
        return eventRepository.findEventById(id);
    }

    @Override
    public GetEventsResponse getAllEvents() {
        var eventEntities = eventRepository.findAll();
        return new GetEventsResponse(
                eventEntities
                        .stream()
                        .map(entity -> new EventDTO(entity))
                        .collect(Collectors.toList()));
    }

    @Override
    // TODO: dodać nawet prostą walidację
    // TODO: przetestować zachowanie dat, nie wiem czy dobrze się ustawiają, strefy czasowe itd 
    public CreateEventResponse save(CreateEventRequest request) throws ApsiValidationException{
        if (request == null || request.name == null || request.name.isBlank())
            throw new ApsiValidationException("Należy podać nazwę wydarzenia", "name");
        
        var loggedUser = (UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var entity = new EventEntity(request, loggedUser.getId());
        var saved = this.eventRepository.save(entity);

        return new CreateEventResponse(new EventDTO(
            saved.getId(),
            saved.getName(),
            saved.getStartDate(),
            saved.getEndDate(),
            saved.getDescription(),
            saved.getOrganizerId()
        ));
    }
}
