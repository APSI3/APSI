package apsi.team3.backend.interfaces;

import apsi.team3.backend.DTOs.Requests.LoginRequest;
import apsi.team3.backend.DTOs.Responses.LoginResponse;
import apsi.team3.backend.exceptions.ApsiException;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.model.UserEntity;

import java.util.Optional;

public interface IUserService {
    Optional<UserEntity> getUserById(Long id);
    Optional<UserEntity> getUserByLogin(String login);
    String hashPassword(String password, String salt) throws ApsiException;
    LoginResponse login(LoginRequest request) throws ApsiValidationException;
}
