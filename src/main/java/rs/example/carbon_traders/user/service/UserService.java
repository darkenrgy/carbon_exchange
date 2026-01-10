package rs.example.carbon_traders.user.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import rs.example.carbon_traders.auth.dto.RegisterRequest;
import rs.example.carbon_traders.user.entity.User;
import rs.example.carbon_traders.user.enums.Role;
import rs.example.carbon_traders.user.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Constructor Injection (Best Practice)
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ===================== USER REGISTRATION =====================
    public User registerUser(RegisterRequest request) {

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        //  Convert & validate role
        Role role = Role.valueOf(request.getRole().toUpperCase());
        if (role == Role.ADMIN) {
            throw new RuntimeException("Admin registration is not allowed");
        }

        // Create User
        User user = new User();
        user.setFullName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setActive(true);

        //  Save to DB
        return userRepository.save(user);
    }

    //  FIND USER BY EMAIL
    // Used for Login & JWT
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // USER VALIDATION (LOGIN)
    public User validateUser(String email, String rawPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isActive()) {
            throw new RuntimeException("User account is disabled");
        }

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }
}


