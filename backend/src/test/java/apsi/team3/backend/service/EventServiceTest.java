package apsi.team3.backend.service;

import apsi.team3.backend.DTOs.EventDTO;
import apsi.team3.backend.model.Event;
import apsi.team3.backend.model.User;
import apsi.team3.backend.model.UserType;
import apsi.team3.backend.repository.EventRepository;
import apsi.team3.backend.services.EventService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Test
    public void testGetEventById() {
        Long eventId = 1L;

        Event event = new Event(
                eventId,
                "",
                null,
                null,
                null,
                null,
                "desc",
                new User(2L, "", "", "", UserType.ORGANIZER, null),
                null
        );
        EventDTO eventDTO = new EventDTO(eventId, "", null, null, null, null, "desc", 2L);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        Optional<EventDTO> result = eventService.getEventById(eventId);
        assertEquals(Optional.of(eventDTO), result);
    }
}
