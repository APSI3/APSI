package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.helpers.QRCodeGenerator;
import apsi.team3.backend.interfaces.ITicketService;
import apsi.team3.backend.repository.TicketRepository;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static apsi.team3.backend.helpers.PaginationValidator.validatePaginationArgs;

@Service
public class TicketService implements ITicketService {
    private final int PAGE_SIZE = 10;
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
    public PaginatedList<TicketDTO> getTicketsByUserId(Long id, LocalDate from, LocalDate to, int pageIndex) throws ApsiValidationException {
        validatePaginationArgs(from, to, pageIndex);

        var page = ticketRepository.getUsersTicketsWithDatesBetween(PageRequest.of(pageIndex, PAGE_SIZE), id, from, to);
        var items = page
                .stream()
                .map(ticket -> {
                    TicketDTO ticketDTO = DTOMapper.toDTO(ticket);
                    try {
                        ticketDTO.setQRCode(QRCodeGenerator.generateQRCode(ticketDTO.toString()));
                    } catch (WriterException | IOException e) {
                        ticketDTO.setQRCode(null);
                    }
                    return ticketDTO;
                }).collect(Collectors.toList());

        return new PaginatedList<>(items, pageIndex, page.getTotalElements(), page.getTotalPages());
    }

    @Override
    public List<TicketDTO> getTicketsByEventId(Long id) throws ApsiValidationException {
        return Arrays.stream(ticketRepository.getTicketsByEventId(id)).map(DTOMapper::toDTO).toList();
    }
}
