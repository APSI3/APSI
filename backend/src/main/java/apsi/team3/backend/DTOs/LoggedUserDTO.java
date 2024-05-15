package apsi.team3.backend.DTOs;

import apsi.team3.backend.model.UserType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class LoggedUserDTO extends UserDTO {
    private String authHeader;
    private String type;
    private String email;

    public LoggedUserDTO(Long id, String login,  String email, String authHeader, UserType type) {
        super(id, login, email);
        this.authHeader = authHeader;
        this.type = type.toString();
    }
}
