package rs.example.carbon_traders.auth.service;

import org.springframework.stereotype.Service;

import rs.example.carbon_traders.security.JwtUtil;
import rs.example.carbon_traders.user.entity.User;
import rs.example.carbon_traders.user.service.UserService;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    public String login(String email, String password) {

        // 1. Validate user credentials
        User user = userService.validateUser(email, password);

        // 2. Generate JWT
        return jwtUtil.generateToken(user);
    }
}

