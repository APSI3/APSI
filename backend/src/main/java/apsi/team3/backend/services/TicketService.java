package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.interfaces.ITicketService;
import apsi.team3.backend.model.MailStructure;
import apsi.team3.backend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketService implements ITicketService {
    private final TicketRepository ticketRepository;
    private final MailService mailSenderService;

    @Autowired
    public TicketService(TicketRepository ticketRepository, MailService mailSenderService) { this.ticketRepository = ticketRepository;
        this.mailSenderService = mailSenderService;
    }

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
}
