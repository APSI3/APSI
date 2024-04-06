package apsi.team3.backend.DTOs;

import apsi.team3.backend.model.UserTypes;

public class LoggedUserDTO extends UserDTO {
    public String authHeader;
    public String type;
    
    public LoggedUserDTO(long id, String login, String authHeader, UserTypes type) {
        super(id, login);
        this.authHeader = authHeader;
        this.type = type.toString();
    }
}
