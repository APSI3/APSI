package apsi.team3.backend.services;

import apsi.team3.backend.interfaces.IEventService;
import apsi.team3.backend.model.EventEntity;
import apsi.team3.backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService implements IEventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) { this.eventRepository = eventRepository; }

    @Override
    public EventEntity getEventById(Long id) { return eventRepository.findEventById(id); }

    @Override
    public List<EventEntity> getAllEvents() { return eventRepository.findAll(); }

    @Override
    public EventEntity save(EventEntity event) {
        return this.eventRepository.save(event);
    }
}
