package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.DTOs.Requests.CreateTicketRequest;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.helpers.MailGenerator;
import apsi.team3.backend.helpers.QRCodeGenerator;
import apsi.team3.backend.interfaces.ITicketService;
import apsi.team3.backend.model.MailStructure;
import apsi.team3.backend.model.User;
import apsi.team3.backend.repository.EventSectionRepository;
import apsi.team3.backend.repository.TicketRepository;
import apsi.team3.backend.repository.TicketTypeRepository;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.google.zxing.WriterException;
import java.io.IOException;
import java.util.*;

import org.springframework.data.domain.PageRequest;
import java.time.LocalDate;
import java.util.stream.Collectors;

import static apsi.team3.backend.helpers.MailGenerator.getDateString;
import static apsi.team3.backend.helpers.MailGenerator.getTimeString;
import static apsi.team3.backend.helpers.PaginationValidator.validatePaginationArgs;

@Service
public class TicketService implements ITicketService {
    private final int PAGE_SIZE = 10;
    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final EventSectionRepository eventSectionRepository;
    private final MailService mailService;

    @Autowired
    public TicketService(TicketRepository ticketRepository, EventSectionRepository eventSectionRepository, TicketTypeRepository ticketTypeRepository, MailService mailService){
        this.ticketRepository = ticketRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.eventSectionRepository = eventSectionRepository;
        this.mailService = mailService;
    }

    @Override
    public Optional<TicketDTO> getTicketById(Long id) {
        return ticketRepository.findById(id).map(DTOMapper::toDTO);
    }

    @Override
    public TicketDTO create(CreateTicketRequest request) throws ApsiValidationException, MessagingException, WriterException, IOException {
        var type = ticketTypeRepository.findById(request.getTicketTypeId());
        if (!type.isPresent())
            throw new ApsiValidationException("Niepoprawny typ biletu", "ticketTypeId");
        
        var section = eventSectionRepository.findById(request.getSectionId());
        if (!section.isPresent())
            throw new ApsiValidationException("Niepoprawny rodzaj miejsca", "sectionId");
        
        var typeCount = ticketTypeRepository.findTicketCount(request.getTicketTypeId()).orElse(0l);
        if (type.get().getQuantityAvailable() <= typeCount)
            throw new ApsiValidationException("Brak dostępnych biletów tego typu", "ticketTypeId");

        var sectionCount = ticketRepository.countTicketsForSectionId(request.getSectionId()).orElse(0l);
        if (section.get().getCapacity() <= sectionCount)
            throw new ApsiValidationException("Brak dostępnych biletów dla tego rodzaju miejsc", "sectionId");
        
        var ticket = DTOMapper.toEntity(request);
        var saved = ticketRepository.save(ticket);
        saved.setTicketType(type.get());
        var dto = DTOMapper.toDTO(saved);

        var QRCode = QRCodeGenerator.generateQRCodeByte(dto.toJSON());
        dto.setQRCode(QRCodeGenerator.convertQRCodeByteToBase64(QRCode));

        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var ticketData = mailService.getTicketContentParams(dto, section.get().getName());
        var mailSubject = "Twój bilet jest tutaj!";
        
        var mailStructure = new MailStructure(mailSubject, QRCode, ticketData);
        mailService.sendTicketMail(user.getEmail(), mailStructure);

        return dto;
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
                    ticketDTO.setQRCode(QRCodeGenerator.generateQRCodeBase64(ticketDTO.toJSON()));
                } catch (WriterException | IOException e) {
                    ticketDTO.setQRCode(null);
                }
                return ticketDTO;
            }).collect(Collectors.toList());

        return new PaginatedList<>(items, pageIndex, page.getTotalElements(), page.getTotalPages());
    }

    @Override
    public List<TicketDTO> getTicketsByEventId(Long id) throws ApsiValidationException {
        var tickets = ticketRepository.getTicketsByEventId(id);
        return Arrays.stream(tickets).map(DTOMapper::toDTO).toList();
    }

    @Override
    public List<TicketDTO> getTicketsByTicketTypeId(Long id) throws ApsiValidationException {
        return Arrays.stream(ticketRepository.getByTicketTypeId(id)).map(DTOMapper::toDTO).toList();
    }

    @Override
    public void deleteByTicketTypeId(Long id) {
        ticketRepository.deleteByTicketTypeId(id);
    }
}
