package apsi.team3.backend.controller;

import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.exceptions.ApsiException;
import apsi.team3.backend.interfaces.IEventService;
import apsi.team3.backend.interfaces.ITicketService;
import apsi.team3.backend.interfaces.ITicketTypeService;
import apsi.team3.backend.model.User;
import apsi.team3.backend.model.UserType;
import apsi.team3.backend.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/ticket_types")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class TicketTypeController {
    private final ITicketTypeService ticketTypeService;
    private final ITicketService ticketService;
    private final MailService mailService;
    private final IEventService eventService;

    @Autowired
    public TicketTypeController(ITicketTypeService ticketTypeService, ITicketService ticketService, MailService mailService, IEventService eventService) {
        this.ticketTypeService = ticketTypeService;
        this.ticketService = ticketService;
        this.mailService = mailService;
        this.eventService = eventService;
    }

    @GetMapping("/{id}/count")
    public ResponseEntity<Long> getTicketCountByTypeId(@PathVariable("id") Long id) {
        var ticketCount = ticketTypeService.getTicketCountByTypeId(id);
        return ResponseEntity.ok(ticketCount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicketType(@PathVariable("id") Long id) throws ApsiException {
        var ticketType = ticketTypeService.getTicketTypeById(id);
        if (ticketType.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var event = eventService.getEventById(ticketType.get().getEventId());
        if (event.get().getOrganizerId() != user.getId() && user.getType() != UserType.SUPERADMIN)
            return ResponseEntity.notFound().build();

        var tickets = ticketService.getTicketsByTicketTypeId(id);
        ticketService.deleteByTicketTypeId(id);
        ticketTypeService.delete(id);

        for (TicketDTO ticket : tickets) {
            mailService.sendEmailTicketTypeDeleted(ticket);
        }
        return ResponseEntity.noContent().build();
    }
}
