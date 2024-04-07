package apsi.team3.backend.DTOs;

import apsi.team3.backend.model.UserTypes;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class LoggedUserDTO extends UserDTO implements Serializable {
    private final String authHeader;
    private final String type;

    public LoggedUserDTO(Long id, String login, String authHeader, UserTypes type) {
        super(id, login);
        this.authHeader = authHeader;
        this.type = type.toString();
    }
}
