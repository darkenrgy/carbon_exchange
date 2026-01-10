package rs.example.carbon_traders.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rs.example.carbon_traders.auth.dto.JwtResponse;
import rs.example.carbon_traders.auth.dto.LoginRequest;
import rs.example.carbon_traders.auth.dto.RegisterRequest;
import rs.example.carbon_traders.auth.service.AuthService;
import rs.example.carbon_traders.user.entity.User;
import rs.example.carbon_traders.user.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService,
                          UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @RequestBody LoginRequest request) {

        String token = authService.login(
                request.getEmail(),
                request.getPassword()
        );

        User user = userService
                .findByEmail(request.getEmail())
                .orElseThrow();

        return ResponseEntity.ok(
                new JwtResponse(token, user.getRole().name())
        );
    }

    //  REGISTER
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request) {

        userService.registerUser(request);

        return ResponseEntity.ok("User registered successfully");
    }
}


