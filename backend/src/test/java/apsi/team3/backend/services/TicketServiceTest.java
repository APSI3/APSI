package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.EventDTO;
import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.TestHelper;
import apsi.team3.backend.model.Ticket;
import apsi.team3.backend.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TicketServiceTest {

    @InjectMocks
    TicketService ticketService;

    @Mock
    TicketRepository ticketRepository;

    @Test
    public void testGetTicketByIdReturnsTicketObject() {
        Long ticketId = 1L;
        var event = DTOMapper.toDTO(TestHelper.getTestEvent());
        var holder = DTOMapper.toDTO(TestHelper.getTestUser());
        var ticketType = DTOMapper.toDTO(TestHelper.getTestTicketType());
        TicketDTO ticketDTO = new TicketDTO(ticketId, ticketType, holder, event, LocalDate.now(), null, null, null);
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(DTOMapper.toEntity(ticketDTO)));
        var actual = ticketService.getTicketById(ticketId).get();
        assertEquals(actual.getId(), ticketDTO.getId());
        assertEquals(actual.getTicketType(), ticketDTO.getTicketType());
        assertEquals(actual.getHolder(), ticketDTO.getHolder());
        assertEquals(actual.getEvent(), ticketDTO.getEvent());
        assertEquals(actual.getHolderFirstName(), ticketDTO.getHolderFirstName());
        assertEquals(actual.getHolderLastName(), ticketDTO.getHolderLastName());
        assertEquals(actual.getPurchaseDate(), ticketDTO.getPurchaseDate());
    }

    @Test
    public void testCreateReturnsCreatedObject() {
        var event = DTOMapper.toDTO(TestHelper.getTestEvent());
        var holder = DTOMapper.toDTO(TestHelper.getTestUser());
        var ticketType = DTOMapper.toDTO(TestHelper.getTestTicketType());
        TicketDTO ticketDTO = new TicketDTO(1L, ticketType, holder, event, LocalDate.now(), "code", "janusz", "kowalski");
        Ticket ticket = DTOMapper.toEntity(ticketDTO);
        when(ticketRepository.save(any())).thenReturn(ticket);
        var actual = ticketService.create(ticketDTO);
        assertEquals(actual.getId(), ticketDTO.getId());
        assertEquals(actual.getTicketType(), ticketDTO.getTicketType());
        assertEquals(actual.getHolder(), ticketDTO.getHolder());
        assertEquals(actual.getEvent(), ticketDTO.getEvent());
        assertEquals(actual.getHolderFirstName(), ticketDTO.getHolderFirstName());
        assertEquals(actual.getHolderLastName(), ticketDTO.getHolderLastName());
        assertEquals(actual.getPurchaseDate(), ticketDTO.getPurchaseDate());
    }
}
