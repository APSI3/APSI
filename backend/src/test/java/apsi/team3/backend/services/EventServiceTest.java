package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.EventDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.model.Event;
import apsi.team3.backend.model.User;
import apsi.team3.backend.model.UserType;
import apsi.team3.backend.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
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
                null,
                null
        );
        EventDTO eventDTO = DTOMapper.toDTO(event);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        Optional<EventDTO> result = eventService.getEventById(eventId);
        assertEquals(Optional.of(eventDTO), result);
    }

    @Test
    public void testGetAllEvents() {
        List<Event> eventList = new ArrayList<>();
        User organizer = new User(1L, "login", "hash", "salt", UserType.ORGANIZER, null);
        eventList.add(new Event(1L, "1", null, null, null, null, "desc", organizer, null, null));
        eventList.add(new Event(2L, "2", null, null, null, null, "desc", organizer, null, null));
        eventList.add(new Event(3L, "3", null, null, null, null, "desc", organizer, null, null));
        when(eventRepository.findAll()).thenReturn(eventList);
        List<EventDTO> eventDTOList = new ArrayList<>();
        for (var event: eventList) {
            eventDTOList.add(DTOMapper.toDTO(event));
        }
        List<EventDTO> result = eventService.getAllEvents();
        assertEquals(eventDTOList, result);
    }

    @Test
    public void testCreateNull() {
        EventDTO nullEventDto = new EventDTO(null, null, null, null, null, null, null, null, null);
        assertThrows(ApsiValidationException.class, () -> eventService.create(nullEventDto));
    }

    @Test
    public void testCreate() {
        EventDTO eventDTO = new EventDTO(null, "name", null, null, null, null, "desc", 1L, null);
        Event event = DTOMapper.toEntity(eventDTO);
        try (var securityContextHolderMockedStatic = mockStatic(SecurityContextHolder.class)) {
            User user = new User(420L, "login", "hash", "salt", UserType.ORGANIZER, null);
            SecurityContext securityContextMock = mock(SecurityContext.class);
            securityContextHolderMockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);
            Authentication authenticationMock = mock(Authentication.class);
            when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
            when(authenticationMock.getPrincipal()).thenReturn(user);
            when(eventRepository.save(any())).thenReturn(event);
            EventDTO result = eventService.create(eventDTO);
            event.setOrganizer(user);
            verify(eventRepository).save(ArgumentMatchers.refEq(event));
            assertEquals(result, eventDTO);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testReplace() throws Exception {
        EventDTO eventDTO = new EventDTO(null, "name", LocalDate.of(2024, 4, 27), null, null, null, "desc", 1L, null);
        when(eventRepository.save(any())).thenReturn(DTOMapper.toEntity(eventDTO));
        assertEquals(eventService.replace(eventDTO), eventDTO);
    }

    @Test
    public void testDelete() {
        Long idToDelete = 18L;  // "How to forget?" #pdk
        eventService.delete(idToDelete);
        verify(eventRepository).deleteById(idToDelete);
    }

    @Test
    public void testNotExist() {
        when(eventRepository.existsById(any())).thenReturn(true);
        assertFalse(eventService.notExists(1L));
        when(eventRepository.existsById(any())).thenReturn(false);
        assertTrue(eventService.notExists(1L));
    }
}
