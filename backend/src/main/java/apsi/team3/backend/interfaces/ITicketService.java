package apsi.team3.backend.interfaces;

import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ITicketService {
    Optional<TicketDTO> getTicketById(Long id);
    TicketDTO create(TicketDTO ticketDTO);
    PaginatedList<TicketDTO> getTicketsByUserId(Long id, LocalDate from, LocalDate to, int pageIndex) throws ApsiValidationException;
    List<TicketDTO> getTicketsByEventId(Long id) throws ApsiValidationException;
}
