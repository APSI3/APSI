package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.LoggedUserDTO;
import apsi.team3.backend.DTOs.Requests.LoginRequest;
import apsi.team3.backend.TestHelper;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.model.User;
import apsi.team3.backend.model.UserType;
import apsi.team3.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    public void testGetUserByIdReturnsUserObject() {
        Long userId = 1234L;
        User user = TestHelper.getTestUser(userId, "login");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        assertEquals(userService.getUserById(userId), Optional.of(user));
    }

    @Test
    public void testGetUserByLoginReturnsUserObject() {
        String login = "andrzej1234";
        User user = TestHelper.getTestUser(1234L, login);
        when(userRepository.findUserByLogin(login)).thenReturn(Optional.of(user));
        assertEquals(userService.getUserByLogin(login), Optional.of(user));
    }

    @Test
    public void testHashPasswordReturnsCorrectHash() throws Exception {
        String password = "apsi";
        String salt = "1234";
        String expectedHash = "098813193d97d1e2ba71c76753eb4a2ce90a6c0229658e168294e619ab00cfec";
        assertEquals(userService.hashPassword(password, salt), expectedHash);
    }

    @Test
    public void testLoginEmptyLoginThrowsException() {
        LoginRequest loginRequest = new LoginRequest("", "apsi");
        assertThrows(ApsiValidationException.class, () -> userService.login(loginRequest));
    }

    @Test
    public void testLoginEmptyPasswordThrowsException() {
        LoginRequest loginRequest = new LoginRequest("apsi", "");
        assertThrows(ApsiValidationException.class, () -> userService.login(loginRequest));
    }

    @Test
    public void testLoginReturnsLoggedUser() throws Exception {
        String login = "apsi";
        String password = "apsi";
        String salt = "1234";
        Long userId = 1234L;
        LoginRequest loginRequest = new LoginRequest(login, password);
        String hash = userService.hashPassword(password, salt);
        User user = new User(userId, login, hash, salt, UserType.PERSON, new ArrayList<>());
        when(userRepository.findUserByLogin(login)).thenReturn(Optional.of(user));
        var encoded = Base64.getEncoder().encodeToString("apsi:apsi".getBytes());
        String expectedAuthHeader = "Basic " + encoded;
        LoggedUserDTO loggedUserDTO = new LoggedUserDTO(userId, login, expectedAuthHeader, UserType.PERSON);
        assertEquals(userService.login(loginRequest), loggedUserDTO);
    }
}
