package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.DTOs.Requests.CreateTicketRequest;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.TestHelper;
import apsi.team3.backend.model.Ticket;
import apsi.team3.backend.repository.TicketRepository;
import jakarta.mail.MessagingException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.google.zxing.WriterException;

import java.io.IOException;
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
    public void testGetTicketByIdReturnsTicketObject() {
        EventServiceTest.mockAuthUser();

        Long ticketId = 1L;
        var event = DTOMapper.toDTO(TestHelper.getTestEvent());
        var holder = DTOMapper.toDTO(TestHelper.getTestUser());
        var section = DTOMapper.toDTO(TestHelper.getTestSection(), 0);
        var ticketType = DTOMapper.toDTO(TestHelper.getTestTicketType());
        TicketDTO ticketDTO = new TicketDTO(ticketId, ticketType, holder, event, LocalDate.now(), null, section.getId(), null, null);
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
    public void testCreateReturnsCreatedObject() throws ApsiValidationException, MessagingException, WriterException, IOException {
        EventServiceTest.mockAuthUser();

        var ticketType = DTOMapper.toDTO(TestHelper.getTestTicketType());
        var section = DTOMapper.toDTO(TestHelper.getTestSection(), 0);
        var createTicketRequest = new CreateTicketRequest(section.getId(), ticketType.getId(), "test", "test");
        Ticket ticket = DTOMapper.toEntity(createTicketRequest);
        when(ticketRepository.save(any())).thenReturn(ticket);
        var actual = ticketService.create(createTicketRequest);
        assertEquals(actual.getTicketType().getId(), createTicketRequest.getTicketTypeId());
        assertEquals(actual.getSectionId(), createTicketRequest.getSectionId());
        assertEquals(actual.getHolderFirstName(), createTicketRequest.getHolderFirstName());
        assertEquals(actual.getHolderLastName(), createTicketRequest.getHolderLastName());
    }
}
