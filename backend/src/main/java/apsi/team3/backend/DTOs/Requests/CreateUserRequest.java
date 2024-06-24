package apsi.team3.backend.DTOs.Requests;

import lombok.Value;

@Value
public class CreateUserRequest {
    String login;
    String email;
    String password;
}
