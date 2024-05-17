package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.interfaces.ITicketService;
import apsi.team3.backend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketService implements ITicketService {
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository) { this.ticketRepository = ticketRepository; }

    @Override
    public Optional<TicketDTO> getTicketById(Long id) {
        return ticketRepository.findById(id).map(DTOMapper::toDTO);
    }

    @Override
    public TicketDTO create(TicketDTO ticketDTO) {
        var ticket = DTOMapper.toEntity(ticketDTO);
        var saved = ticketRepository.save(ticket);
        return DTOMapper.toDTO(saved);
    }

    @Override
    public List<TicketDTO> getTicketsByUserId(Long id) {
        return ticketRepository.findByHolderId(id).stream().map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}
