package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.TicketTypeDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.ITicketTypeService;
import apsi.team3.backend.repository.TicketTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketTypeService implements ITicketTypeService {
    private final TicketTypeRepository ticketTypeRepository;

    @Autowired
    public TicketTypeService(TicketTypeRepository ticketTypeRepository) { this.ticketTypeRepository = ticketTypeRepository; }

    @Override
    public Optional<TicketTypeDTO> getTicketTypeById(Long id) {
        return ticketTypeRepository.findById(id).map(DTOMapper::toDTO);
    }

    @Override
    public Optional<Long> getTicketCountByTypeId(Long id) {
        return ticketTypeRepository.findTicketCount(id);
    }

    @Override
    public List<TicketTypeDTO> getTicketTypeByEventId(Long eventId) {
        return ticketTypeRepository
                .findByEventId(eventId)
                .stream().map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TicketTypeDTO create(TicketTypeDTO ticketTypeDTO) throws ApsiValidationException {
        var ticketType = DTOMapper.toEntity(ticketTypeDTO);
        var saved = ticketTypeRepository.save(ticketType);
        return DTOMapper.toDTO(saved);
    }

    @Override
    public TicketTypeDTO replace(TicketTypeDTO ticketType) {
        var entity = DTOMapper.toEntity(ticketType);
        var saved = ticketTypeRepository.save(entity);

        return DTOMapper.toDTO(saved);
    }

    @Override
    public void delete(Long id) {
        ticketTypeRepository.deleteById(id);
    }

    @Override
    public boolean notExists(Long id) {
        return !ticketTypeRepository.existsById(id);
    }
}
