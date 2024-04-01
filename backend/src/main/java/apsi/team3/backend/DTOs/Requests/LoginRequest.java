package apsi.team3.backend.DTOs.Requests;

public class LoginRequest {
    public String login;
    public String password;

    public LoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
