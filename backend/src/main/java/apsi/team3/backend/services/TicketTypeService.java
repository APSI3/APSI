package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.TicketTypeDTO;
import apsi.team3.backend.exceptions.ApsiException;
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
    public TicketTypeService(TicketTypeRepository ticketTypeRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
    }

    @Override
    public Optional<TicketTypeDTO> getTicketTypeById(Long id) {
        return ticketTypeRepository.findById(id).map(DTOMapper::toDTO);
    }

    @Override
    public Optional<Long> getTicketCountByTypeId(Long id) {
        return ticketTypeRepository.findTicketCount(id);
    }

    @Override
    public List<TicketTypeDTO> getTicketTypesByEventId(Long eventId) {
        return ticketTypeRepository
                .findByEventId(eventId)
                .stream().map(DTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TicketTypeDTO create(TicketTypeDTO ticketTypeDTO) throws ApsiValidationException {
        var ticketType = DTOMapper.toEntity(ticketTypeDTO, null);
        var saved = ticketTypeRepository.save(ticketType);
        return DTOMapper.toDTO(saved);
    }

    @Override
    public TicketTypeDTO replace(TicketTypeDTO ticketType) {
        var entity = DTOMapper.toEntity(ticketType, null);
        var saved = ticketTypeRepository.save(entity);

        return DTOMapper.toDTO(saved);
    }

    @Override
    public void delete(Long id) throws ApsiException {
        // we check in controller if ticketType exists
        var ticketType = ticketTypeRepository.findById(id);
        var event = ticketType.get().getEvent();
        var eventTicketTypeCount = (long) event.getTicketTypes().size();

        if (eventTicketTypeCount <= 1) {
            throw new ApsiException("Nie można usunąć typu biletu, jeśli wydarzenie ma mniej niż dwa typy biletów");
        }
        ticketTypeRepository.deleteById(id);
    }

    @Override
    public boolean notExists(Long id) {
        return !ticketTypeRepository.existsById(id);
    }
}
