package apsi.team3.backend.DTOs.Requests;

import lombok.Value;

@Value
public class LoginRequest {
    String login;
    String password;
}
