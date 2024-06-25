package apsi.team3.backend.controller;

import apsi.team3.backend.DTOs.LoggedUserDTO;
import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.DTOs.Requests.CreateUserRequest;
import apsi.team3.backend.DTOs.Requests.LoginRequest;
import apsi.team3.backend.DTOs.UserDTO;
import apsi.team3.backend.exceptions.ApsiException;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public LoggedUserDTO Login(@RequestBody LoginRequest request) throws ApsiValidationException {
        return userService.login(request);
    }

    @PostMapping()
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserRequest request) throws ApsiException {
        var newUser = userService.createUser(request);
        return ResponseEntity.ok(newUser);
    }

    @GetMapping
    public ResponseEntity<PaginatedList<UserDTO>> getUsers(@RequestParam int pageIndex) throws ApsiValidationException {
        var allUsers = userService.getUsers(pageIndex);
        return ResponseEntity.ok(allUsers);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
