package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.EventDTO;
import apsi.team3.backend.TestHelper;
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
    public void testGetEventByIdReturnsEvent() {
        Long eventId = 1L;
        Event event = TestHelper.getTestEvent(eventId);
        EventDTO eventDTO = DTOMapper.toDTO(event);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        Optional<EventDTO> result = eventService.getEventById(eventId);
        assertEquals(Optional.of(eventDTO), result);
    }

    @Test
    public void testGetAllEventsReturnsListOfAllEvents() {
        List<Event> eventList = new ArrayList<>();
        eventList.add(TestHelper.getTestEvent(1L));
        eventList.add(TestHelper.getTestEvent(2L));
        eventList.add(TestHelper.getTestEvent(2L, "some name"));
        when(eventRepository.findAll()).thenReturn(eventList);
        List<EventDTO> eventDTOList = new ArrayList<>();
        for (var event: eventList) {
            eventDTOList.add(DTOMapper.toDTO(event));
        }
        List<EventDTO> result = eventService.getAllEvents();
        assertEquals(eventDTOList, result);
    }

    @Test
    public void testCreateEventWithNullNameThrowsException() {
        EventDTO nullEventDto = DTOMapper.toDTO(TestHelper.getTestEvent(null, null));
        assertThrows(ApsiValidationException.class, () -> eventService.create(nullEventDto));
    }

    @Test
    public void testCreateReturnsCreatedEvent() {
        Event event = TestHelper.getTestEvent(null);
        EventDTO eventDTO = DTOMapper.toDTO(event);
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
    public void testReplaceReturnsReplacedEvent() throws Exception {
        Event event = TestHelper.getTestEvent(null, "changed name");
        EventDTO eventDTO = DTOMapper.toDTO(event);
        when(eventRepository.save(any())).thenReturn(event);
        assertEquals(eventService.replace(eventDTO), eventDTO);
    }

    @Test
    public void testDeleteCallsEventRepositoryDeleteById() {
        Long idToDelete = 18L;  // "How to forget?" #pdk
        eventService.delete(idToDelete);
        verify(eventRepository).deleteById(idToDelete);
    }

    @Test
    public void testNotExistReturnsStateOfEventExistence() {
        when(eventRepository.existsById(any())).thenReturn(true);
        assertFalse(eventService.notExists(1L));
        when(eventRepository.existsById(any())).thenReturn(false);
        assertTrue(eventService.notExists(1L));
    }
}
