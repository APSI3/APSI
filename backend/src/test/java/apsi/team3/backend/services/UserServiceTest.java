package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.LoggedUserDTO;
import apsi.team3.backend.DTOs.Requests.LoginRequest;
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
    public void testGetUserById() {
        Long userId = 1234L;
        User user = new User(userId, "login", "hash", "pepper", UserType.PERSON, new ArrayList<>());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        assertEquals(userService.getUserById(userId), Optional.of(user));
    }

    @Test
    public void testGetUserByLogin() {
        String login = "andrzej1234";
        User user = new User(1234L, login, "hash", "pepper", UserType.PERSON, new ArrayList<>());
        when(userRepository.findUserByLogin(login)).thenReturn(Optional.of(user));
        assertEquals(userService.getUserByLogin(login), Optional.of(user));
    }

    @Test
    public void testHashPassword() {
        String password = "apsi";
        String salt = "1234";
        String expectedHash = "098813193d97d1e2ba71c76753eb4a2ce90a6c0229658e168294e619ab00cfec";
        try {
            assertEquals(userService.hashPassword(password, salt), expectedHash);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testLoginEmptyLogin() {
        LoginRequest loginRequest = new LoginRequest("", "apsi");
        assertThrows(ApsiValidationException.class, () -> userService.login(loginRequest));
    }

    @Test
    public void testLoginEmptyPassword() {
        LoginRequest loginRequest = new LoginRequest("apsi", "");
        assertThrows(ApsiValidationException.class, () -> userService.login(loginRequest));
    }

    @Test
    public void testLogin() {
        String login = "apsi";
        String password = "apsi";
        String salt = "1234";
        Long userId = 1234L;
        LoginRequest loginRequest = new LoginRequest(login, password);
        try {
            String hash = userService.hashPassword(password, salt);
            User user = new User(userId, login, hash, salt, UserType.PERSON, new ArrayList<>());
            when(userRepository.findUserByLogin(login)).thenReturn(Optional.of(user));
            var encoded = Base64.getEncoder().encodeToString("apsi:apsi".getBytes());
            String expectedAuthHeader = "Basic " + encoded;
            LoggedUserDTO loggedUserDTO = new LoggedUserDTO(userId, login, expectedAuthHeader, UserType.PERSON);
            assertEquals(userService.login(loginRequest), loggedUserDTO);
        } catch (Exception e) {
            fail();
        }
    }
}