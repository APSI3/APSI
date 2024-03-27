package apsi.team3.backend.DTOs.Responses;

import apsi.team3.backend.DTOs.UserDTO;

public class LoginResponse {
    public UserDTO User;

    public LoginResponse(UserDTO user) {
        User = user;
    }
}
