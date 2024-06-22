package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.DTOs.Requests.CreateTicketRequest;
import apsi.team3.backend.exceptions.ApsiValidationException;
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
import java.util.Map;
import java.util.Optional;

import static apsi.team3.backend.helpers.MailGenerator.getDateString;

@Service
public class TicketService implements ITicketService {
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
        var dto = DTOMapper.toDTO(saved);

        var QRCode = QRCodeGenerator.generateQRCode(dto.toString());
        dto.setQRCode(QRCode);

        var event = type.get().getEvent();
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var mailSubject = "Twój bilet jest tutaj!";
        var ticketData =  Map.of(
            "eventName", event.getName(),
            "date", getDateString(event.getStartDate(), event.getEndDate()),
            "ticketType", type.get().getName(),
            "price", type.get().getPrice().toString(),
            "holderName", user.getLogin(),
            "sectionName", section.get().getName()
        );
        
        var mailStructure = new MailStructure(
            mailSubject,
            dto.getQRCode(),
            QRCode,
            ticketData
        );
        mailService.sendMail(user.getEmail(), mailStructure);

        return dto;
    }
}
