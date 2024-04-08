package apsi.team3.backend.DTOs;

import apsi.team3.backend.model.UserType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class LoggedUserDTO extends UserDTO {
    private final String authHeader;
    private final String type;

    public LoggedUserDTO(Long id, String login, String authHeader, UserType type) {
        super(id, login);
        this.authHeader = authHeader;
        this.type = type.toString();
    }
}
