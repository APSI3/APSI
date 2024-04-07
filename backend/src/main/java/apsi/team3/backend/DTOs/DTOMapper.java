package apsi.team3.backend.DTOs;

import apsi.team3.backend.model.EventEntity;
import apsi.team3.backend.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {

    public static EventEntity toEntity(EventDTO event) {
        return EventEntity.builder()
                .id(event.getId())
                .name(event.getName())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .description(event.getDescription())
                .organizerId(event.getOrganizerId())
                .build();
    }

    public static EventDTO toDTO(EventEntity event) {
        return new EventDTO(
                event.getId(),
                event.getName(),
                event.getStartDate(),
                event.getEndDate(),
                event.getDescription(),
                event.getOrganizerId()
        );
    }

    public static LoggedUserDTO toDTO(UserEntity user, String header) {
        return new LoggedUserDTO(user.getId(), user.getLogin(), header);
    }

    public static UserDTO toDTO(UserEntity user) {
        return new UserDTO(user.getId(), user.getLogin());
    }

}
