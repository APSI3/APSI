package apsi.team3.backend.interfaces;

import apsi.team3.backend.DTOs.TicketDTO;

import java.util.Optional;

public interface ITicketService {
    Optional<TicketDTO> getTicketById(Long id);
    TicketDTO create(TicketDTO ticketDTO);
}
