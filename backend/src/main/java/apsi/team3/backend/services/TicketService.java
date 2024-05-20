package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.ExtendedTicketDTO;
import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.helpers.QRCodeGenerator;
import apsi.team3.backend.interfaces.ITicketService;
import apsi.team3.backend.model.Event;
import apsi.team3.backend.model.TicketType;
import apsi.team3.backend.repository.TicketRepository;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
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
    public PaginatedList<ExtendedTicketDTO> getTicketsByUserId(Long id, LocalDate from, LocalDate to, int pageIndex) throws ApsiValidationException {
        validatePaginationArgs(from, to, pageIndex);

        var page = ticketRepository.getUsersTicketsWithDatesBetween(PageRequest.of(pageIndex, PAGE_SIZE), id, from, to);
        var items = page
                .stream()
                .map(ticket -> {
                    TicketType ticketType = ticket.getTicketType();
                    Event event = ticketType.getEvent();
                    ExtendedTicketDTO ticketDTO = new ExtendedTicketDTO(
                            ticket.getId(),
                            ticketType.getId(),
                            ticket.getHolder().getId(),
                            event.getId(),
                            ticket.getPurchaseDate(),
                            null,
                            ticket.getHolderFirstName(),
                            ticket.getHolderLastName(),
                            event.getName(),
                            event.getStartDate(),
                            event.getStartTime(),
                            event.getEndDate(),
                            event.getEndTime(),
                            ticketType.getName(),
                            ticketType.getPrice()
                    );
                    try {
                        ticketDTO.setQRCode(QRCodeGenerator.generateQRCode(ticketDTO.toString()));
                    } catch (WriterException | IOException e) {
                        ticketDTO.setQRCode(null);
                    }
                    return ticketDTO;
                }).collect(Collectors.toList());

        return new PaginatedList<>(items, pageIndex, page.getTotalElements(), page.getTotalPages());
    }
}
