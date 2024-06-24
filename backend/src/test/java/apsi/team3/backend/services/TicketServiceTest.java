package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.TestHelper;
import apsi.team3.backend.model.Event;
import apsi.team3.backend.model.User;
import apsi.team3.backend.repository.TicketRepository;
import apsi.team3.backend.repository.TicketTypeRepository;

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

    @Mock
    TicketTypeRepository ticketTypeRepository;

    @Test
    public void testGetTicketByIdReturnsTicketObject() {
        EventServiceTest.mockAuthUser();

        Long ticketId = 1L;
        var event = DTOMapper.toDTO(TestHelper.getTestEvent());
        var holder = DTOMapper.toDTO(TestHelper.getTestUser());
        var section = DTOMapper.toDTO(TestHelper.getTestSection(), 0);
        var ticketType = DTOMapper.toDTO(TestHelper.getTestTicketType());
        TicketDTO ticketDTO = new TicketDTO(ticketId, ticketType, holder, event, LocalDate.now(), null, section.getId(), null, null);
        var entity = DTOMapper.toEntity(ticketDTO);
        entity.getTicketType().setEvent(Event.builder().id(123L).build());
        entity.getTicketType().getEvent().setOrganizer(User.builder().id(1234L).build());
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(entity));
        var actual = ticketService.getTicketById(ticketId).get();

        assertEquals(actual.getHolderFirstName(), ticketDTO.getHolderFirstName());
        assertEquals(actual.getHolderLastName(), ticketDTO.getHolderLastName());
        assertEquals(actual.getPurchaseDate(), ticketDTO.getPurchaseDate());
    }
}
