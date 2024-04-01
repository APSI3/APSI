package apsi.team3.backend.DTOs.Responses;

import apsi.team3.backend.DTOs.LoggedUserDTO;

public class LoginResponse {
    public LoggedUserDTO user;

    public LoginResponse(LoggedUserDTO user) {
        this.user = user;
    }
}
