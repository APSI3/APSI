package apsi.team3.backend.DTOs;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class LoggedUserDTO extends UserDTO implements Serializable {
    private final String authHeader;

    public LoggedUserDTO(Long id, String login, String authHeader) {
        super(id, login);
        this.authHeader = authHeader;
    }
}
