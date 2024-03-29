package apsi.team3.backend.interfaces;

import apsi.team3.backend.DTOs.Requests.CreateEventRequest;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.model.EventEntity;

import java.util.List;
import java.util.Optional;

public interface IEventService {
    Optional<EventEntity> getEventById(Long id);
    List<EventEntity> getAllEvents();
    void save(CreateEventRequest event) throws ApsiValidationException;
}
