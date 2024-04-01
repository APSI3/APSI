package apsi.team3.backend.DTOs;

public class UserDTO {
    public long id;
    public String login;
    
    public UserDTO(long id, String login) {
        this.id = id;
        this.login = login;
    }
}
