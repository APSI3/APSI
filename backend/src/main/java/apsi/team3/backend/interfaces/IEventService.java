package apsi.team3.backend.interfaces;

import apsi.team3.backend.model.EventEntity;

import java.util.List;

public interface IEventService {
    EventEntity getEventById(Long id);
    List<EventEntity> getAllEvents();
    EventEntity save(EventEntity event);
}
