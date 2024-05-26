package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.EventDTO;
import apsi.team3.backend.TestHelper;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.model.Event;
import apsi.team3.backend.model.TicketType;
import apsi.team3.backend.model.User;
import apsi.team3.backend.model.UserType;
import apsi.team3.backend.repository.EventRepository;
import apsi.team3.backend.repository.TicketTypeRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class EventServiceTest {
    private static User mockedUser = null;

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TicketTypeRepository ticketTypeRepository;

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
    public void testGetAllEventsReturnsListOfAllEvents() throws ApsiValidationException {
        List<Event> eventList = new ArrayList<>();
        eventList.add(TestHelper.getTestEvent(1L));
        eventList.add(TestHelper.getTestEvent(2L));
        eventList.add(TestHelper.getTestEvent(2L, "some name"));

        var from = LocalDate.now();
        var to = from.plusDays(7);
        var pager = PageRequest.of(0, 10);

        when(eventRepository.getEventsWithDatesBetween(pager, from, to)).thenReturn(new PageImpl<Event>(eventList, pager, eventList.size()));
        List<EventDTO> eventDTOList = new ArrayList<>();
        for (var event: eventList) {
            eventDTOList.add(DTOMapper.toDTO(event));
        }
        var result = eventService.getEvents(from, to, 0);
        assertEquals(eventDTOList, result.items);
    }

    @Test
    public void testCreateEventWithNullNameThrowsException() {
        mockAuthUser();
        EventDTO nullEventDto = DTOMapper.toDTO(TestHelper.getTestEvent(null, null));
        assertThrows(ApsiValidationException.class, () -> eventService.create(nullEventDto, null));
    }

    private User mockAuthUser(){
        if (mockedUser != null)
            return mockedUser;

        var securityContextHolderMockedStatic = mockStatic(SecurityContextHolder.class);
        User user = new User(420L, "login", "hash", "salt", UserType.ORGANIZER, "email", null);
        var securityContextMock = mock(SecurityContext.class);
        securityContextHolderMockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);
        var authenticationMock = mock(Authentication.class);
        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        when(authenticationMock.getPrincipal()).thenReturn(user);
        mockedUser = user;
        return user;
    }

    @Test
    public void testCreateReturnsCreatedEvent() {
        var event = TestHelper.getTestEvent(null);
        event.getTicketTypes().add(new TicketType(null, event, "type", BigDecimal.valueOf(50), 50));
        var eventDTO = DTOMapper.toDTO(event);
        try {
            var user = mockAuthUser();
            when(eventRepository.save(any())).thenReturn(event);  
            when(ticketTypeRepository.saveAll(any())).thenReturn(event.getTicketTypes());  
            var result = eventService.create(eventDTO, null);
            event.setOrganizer(user);
            assertEquals(result.getDescription(), eventDTO.getDescription());
            assertEquals(result.getEndDate(), eventDTO.getEndDate());
            assertEquals(result.getStartDate(), eventDTO.getStartDate());
            assertEquals(result.getTicketTypes(), eventDTO.getTicketTypes());
            assertEquals(result.getLocation(), eventDTO.getLocation());
            assertEquals(result.getName(), eventDTO.getName());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testReplaceReturnsReplacedEvent() throws Exception {
        mockAuthUser();
        Event event = TestHelper.getTestEvent(null, "changed name");
        event.getTicketTypes().add(new TicketType(null, event, "type", BigDecimal.valueOf(50), 50));
        EventDTO eventDTO = DTOMapper.toDTO(event);
        when(eventRepository.save(any())).thenReturn(event);
        assertEquals(eventService.replace(eventDTO, null), eventDTO);
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
