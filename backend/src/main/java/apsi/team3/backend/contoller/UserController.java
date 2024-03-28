package apsi.team3.backend.contoller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apsi.team3.backend.DTOs.ApiResponse;
import apsi.team3.backend.DTOs.Requests.LoginRequest;
import apsi.team3.backend.DTOs.Responses.LoginResponse;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IUserService;

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
    public ApiResponse<LoginResponse> Login(@RequestBody LoginRequest request){
        try {
            var resp = userService.login(request);
            return new ApiResponse<>(resp);
        }
        catch (ApsiValidationException e){
            return new ApiResponse<>(new HashMap<String, String>() {{ 
                put(e.key, e.getMessage());
            }});
        }
    }
}
