package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.TicketTypeDTO;
import apsi.team3.backend.exceptions.ApsiException;
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
        TicketType ticketType = DTOMapper.toEntity(ticketTypeDTO, null);
        when(ticketTypeRepository.save(any())).thenReturn(ticketType);
        assertEquals(ticketTypeService.create(ticketTypeDTO), ticketTypeDTO);
        verify(ticketTypeRepository).save(ArgumentMatchers.refEq(ticketType));
    }

    @Test
    public void testDeleteCallsDeleteByIdRepositoryMethod() throws ApsiException {
        Event event = new Event();
        TicketType ticketType = new TicketType();
        ticketType.setId(1L);
        ticketType.setEvent(event);
        event.setTicketTypes(List.of(ticketType, new TicketType()));
        when(ticketTypeRepository.findById(anyLong())).thenReturn(Optional.of(ticketType));
        ticketTypeService.delete(1L);
        verify(ticketTypeRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteDoesNotCallDeleteByIdRepositoryMethodIfEventHasOnlyOneTicketType() throws ApsiException {
        Event event = new Event();
        TicketType ticketType = new TicketType();
        ticketType.setId(1L);
        ticketType.setEvent(event);
        event.setTicketTypes(List.of(ticketType));
        when(ticketTypeRepository.findById(anyLong())).thenReturn(Optional.of(ticketType));
        assertThrows(ApsiException.class, () -> ticketTypeService.delete(1L));
    }

    @Test
    public void testNotExistReturnsStateOfObjectExistence() {
        when(ticketTypeRepository.existsById(any())).thenReturn(true);
        assertFalse(ticketTypeService.notExists(1L));
        when(ticketTypeRepository.existsById(any())).thenReturn(false);
        assertTrue(ticketTypeService.notExists(1L));
    }
}
