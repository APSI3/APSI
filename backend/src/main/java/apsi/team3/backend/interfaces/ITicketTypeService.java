package apsi.team3.backend.interfaces;

import apsi.team3.backend.DTOs.TicketTypeDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;

import java.util.List;
import java.util.Optional;

public interface ITicketTypeService {
    Optional<TicketTypeDTO> getTicketTypeById(Long id);
    Optional<Long> getTicketCountByTypeId(Long id);
    List<TicketTypeDTO> getTicketTypeByEventId(Long eventId);
    TicketTypeDTO create(TicketTypeDTO ticketType) throws ApsiValidationException;
    TicketTypeDTO replace(TicketTypeDTO ticketType) throws ApsiValidationException;

    void delete(Long id);

    boolean notExists(Long id);
}
