package apsi.team3.backend.services;

import apsi.team3.backend.exceptions.ApsiValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import apsi.team3.backend.DTOs.EventReportDTO;
import apsi.team3.backend.interfaces.IReportService;
import apsi.team3.backend.model.User;
import apsi.team3.backend.model.UserType;

@Service
public class ReportService implements IReportService {
    private final EventService eventService;
    private final TicketService ticketService;

    @Autowired
    public ReportService(TicketService ticketService, EventService eventService) { 
        this.ticketService = ticketService; 
        this.eventService = eventService;
    }

    @Override
    public EventReportDTO getEventReport(Long id) throws ApsiValidationException {
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var event = eventService.getEventById(id);
        if (!event.isPresent() || (event.get().getOrganizerId() != user.getId() && user.getType() != UserType.SUPERADMIN))
            throw new ApsiValidationException("Nie znaleziono wydarzenia o takim id", "id");
        
        var tickets = ticketService.getTicketsByEventId(id);
        return new EventReportDTO(event.get(), tickets);
    }
}
