package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.TicketTypeDTO;
import apsi.team3.backend.model.*;
import apsi.team3.backend.repository.TicketTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TicketTypeServiceTest {
    @Mock
    TicketTypeRepository ticketTypeRepository;

    @InjectMocks
    TicketTypeService ticketTypeService;

    @Test
    public void testGetTicketTypeByIdReturnsTicketTypeObject() {
        Long ticketTypeId = 1L;
        TicketType ticketType = new TicketType(ticketTypeId, new Event(), "name", null, 10);
        TicketTypeDTO ticketTypeDTO = DTOMapper.toDTO(ticketType);
        when(ticketTypeRepository.findById(ticketTypeId)).thenReturn(Optional.of(ticketType));
        assertEquals(ticketTypeService.getTicketTypeById(ticketTypeId), Optional.of(ticketTypeDTO));
    }

    @Test
    public void testGetTicketTypeByEventIdReturnsListOfTicketTypes() {
        Long eventId = 1L;
        List<TicketType> ticketTypeList = new ArrayList<>();
        ticketTypeList.add(new TicketType(1L, new Event(), "name", null, 10));
        ticketTypeList.add(new TicketType(2L, new Event(), "name", null, 10));
        List<TicketTypeDTO> ticketTypeDTOList = new ArrayList<>();
        for (var ticketType : ticketTypeList) {
            ticketTypeDTOList.add(DTOMapper.toDTO(ticketType));
        }
        when(ticketTypeRepository.findByEventId(eventId)).thenReturn(ticketTypeList);
        assertEquals(ticketTypeService.getTicketTypesByEventId(eventId), ticketTypeDTOList);
    }

    @Test
    public void testCreateReturnsCreatedTicketTypeObject() throws Exception {
        TicketTypeDTO ticketTypeDTO = new TicketTypeDTO(1L, 1L, "name", BigDecimal.valueOf(100L), 10);
        TicketType ticketType = DTOMapper.toEntity(ticketTypeDTO);
        when(ticketTypeRepository.save(any())).thenReturn(ticketType);
        assertEquals(ticketTypeService.create(ticketTypeDTO), ticketTypeDTO);
        verify(ticketTypeRepository).save(ArgumentMatchers.refEq(ticketType));
    }

    @Test
    public void testReplaceReturnsReplacedTicketTypeObject() {
        TicketTypeDTO ticketTypeDTO = new TicketTypeDTO(1L, 1L, "name", BigDecimal.valueOf(100L), 10);
        TicketType ticketType = DTOMapper.toEntity(ticketTypeDTO);
        when(ticketTypeRepository.save(any())).thenReturn(ticketType);
        assertEquals(ticketTypeService.replace(ticketTypeDTO), ticketTypeDTO);
        verify(ticketTypeRepository).save(ArgumentMatchers.refEq(ticketType));
    }

    @Test
    public void testDeleteCallsDeleteByIdRepositoryMethod() {
        Long ticketTypeId = 2L;
        ticketTypeService.delete(ticketTypeId);
        verify(ticketTypeRepository).deleteById(ticketTypeId);
    }

    @Test
    public void testNotExistReturnsStateOfObjectExistence() {
        when(ticketTypeRepository.existsById(any())).thenReturn(true);
        assertFalse(ticketTypeService.notExists(1L));
        when(ticketTypeRepository.existsById(any())).thenReturn(false);
        assertTrue(ticketTypeService.notExists(1L));
    }
}
