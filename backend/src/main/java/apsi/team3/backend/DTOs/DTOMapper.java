package apsi.team3.backend.DTOs;

import apsi.team3.backend.model.Event;
import apsi.team3.backend.model.TicketType;
import apsi.team3.backend.model.User;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {

    public static Event toEntity(EventDTO event) {
        User organizer = User.builder().id(event.getOrganizerId()).build();
        return Event.builder()
                .id(event.getId())
                .name(event.getName())
                .startDate(event.getStartDate())
                .startTime(event.getStartTime())
                .endDate(event.getEndDate())
                .endTime(event.getEndTime())
                .description(event.getDescription())
                .organizer(organizer)
                .build();
    }

    public static TicketType toEntity(TicketTypeDTO ticketType) {
        Event event = Event.builder().id(ticketType.getEventId()).build();
        return TicketType.builder()
                .id(ticketType.getId())
                .event(event)
                .name(ticketType.getName())
                .price(ticketType.getPrice())
                .quantityAvailable(ticketType.getQuantityAvailable())
                .build();
    }

    public static EventDTO toDTO(Event event) {
        return new EventDTO(
                event.getId(),
                event.getName(),
                event.getStartDate(),
                event.getStartTime(),
                event.getEndDate(),
                event.getEndTime(),
                event.getDescription(),
                event.getOrganizer().getId()
        );
    }

    public static LoggedUserDTO toDTO(User user, String header) {
        return new LoggedUserDTO(user.getId(), user.getLogin(), header, user.getType());
    }

    public static UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getLogin());
    }

    public static TicketTypeDTO toDTO(TicketType ticketType) {
        return new TicketTypeDTO(
                ticketType.getId(),
                ticketType.getEvent().getId(),
                ticketType.getName(),
                ticketType.getPrice(),
                ticketType.getQuantityAvailable()
        );
    }
}
