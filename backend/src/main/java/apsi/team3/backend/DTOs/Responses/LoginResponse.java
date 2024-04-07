package apsi.team3.backend.DTOs.Responses;

import apsi.team3.backend.DTOs.LoggedUserDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginResponse {
    public LoggedUserDTO user;
}
