package apsi.team3.backend.interfaces;

import apsi.team3.backend.DTOs.LoggedUserDTO;
import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.DTOs.Requests.CreateUserRequest;
import apsi.team3.backend.DTOs.Requests.LoginRequest;
import apsi.team3.backend.DTOs.UserDTO;
import apsi.team3.backend.exceptions.ApsiException;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.model.Form;
import apsi.team3.backend.model.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> getUserById(Long id);

    Optional<User> getUserByLogin(String login);

    LoggedUserDTO login(LoginRequest request) throws ApsiValidationException;

    PaginatedList<UserDTO> getUsers(int pageIndex) throws ApsiValidationException;

    Void deleteUser(Long id);

    UserDTO createUser(CreateUserRequest request) throws ApsiException;

    int getUserLoginCount(String login);

    UserDTO createOrganizer(Form form);
}
