package apsi.team3.backend.controller;

import apsi.team3.backend.DTOs.Requests.LoginRequest;
import apsi.team3.backend.DTOs.Responses.LoginResponse;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class UserController {
    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/session")
    public boolean Session() {
        var a = SecurityContextHolder.getContext().getAuthentication();
        return a.isAuthenticated();
    }

    @PostMapping("/login")
    public LoginResponse Login(@RequestBody LoginRequest request) throws ApsiValidationException {
        return userService.login(request);
    }
}
