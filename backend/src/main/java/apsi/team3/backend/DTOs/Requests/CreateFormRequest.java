package apsi.team3.backend.DTOs.Requests;

import lombok.Getter;

@Getter
public class CreateFormRequest {
    private String login;
    private String email;
    private String password;
}
