package apsi.team3.backend.interfaces;

import apsi.team3.backend.DTOs.EventDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;

import java.util.List;
import java.util.Optional;

public interface IEventService {
    Optional<EventDTO> getEventById(Long id);

    List<EventDTO> getAllEvents();

    EventDTO save(EventDTO event) throws ApsiValidationException;
}
