package apsi.team3.backend.interfaces;

import apsi.team3.backend.DTOs.LoggedUserDTO;
import apsi.team3.backend.DTOs.Requests.LoginRequest;
import apsi.team3.backend.exceptions.ApsiException;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.model.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> getUserById(Long id);

    Optional<User> getUserByLogin(String login);

    String hashPassword(String password, String salt) throws ApsiException;

    LoggedUserDTO login(LoginRequest request) throws ApsiValidationException;
}
