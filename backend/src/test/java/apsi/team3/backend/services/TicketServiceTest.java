package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.model.Ticket;
import apsi.team3.backend.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
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
    public void testGetTicketById() {
        Long ticketId = 1L;
        TicketDTO ticketDTO = new TicketDTO(ticketId, null, null, null);
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(DTOMapper.toEntity(ticketDTO)));
        assertEquals(ticketService.getTicketById(ticketId), Optional.of(ticketDTO));
    }

    @Test
    public void testCreate() {
        TicketDTO ticketDTO = new TicketDTO(1L, 1L, 1L, LocalDate.of(2024, 4, 27));
        Ticket ticket = DTOMapper.toEntity(ticketDTO);
        when(ticketRepository.save(any())).thenReturn(ticket);
        assertEquals(ticketService.create(ticketDTO), ticketDTO);
    }
}
