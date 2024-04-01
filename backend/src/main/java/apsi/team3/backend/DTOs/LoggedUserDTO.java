package apsi.team3.backend.DTOs;

public class LoggedUserDTO extends UserDTO {
    public String authHeader;
    
    public LoggedUserDTO(long id, String login, String authHeader) {
        super(id, login);
        this.authHeader = authHeader;
    }
}
