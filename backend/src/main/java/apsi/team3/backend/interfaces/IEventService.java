package apsi.team3.backend.interfaces;

import apsi.team3.backend.DTOs.Requests.CreateEventRequest;
import apsi.team3.backend.DTOs.Responses.CreateEventResponse;
import apsi.team3.backend.DTOs.Responses.GetEventsResponse;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.model.EventEntity;

import java.util.List;
import java.util.Optional;

public interface IEventService {
    Optional<EventEntity> getEventById(Long id);
    GetEventsResponse getAllEvents();
    CreateEventResponse save(CreateEventRequest event) throws ApsiValidationException;
}
