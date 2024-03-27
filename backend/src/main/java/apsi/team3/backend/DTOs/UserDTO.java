package apsi.team3.backend.DTOs;

public class UserDTO {
    public long Id;
    public String Login;
    
    public UserDTO(long id, String login) {
        Id = id;
        Login = login;
    }
}
