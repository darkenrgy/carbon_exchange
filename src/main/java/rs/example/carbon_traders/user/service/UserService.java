package rs.example.carbon_traders.user.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

//       USER REGISTRATION
    public User registerUser(String fullName,
                             String email,
                             String rawPassword,
                             Role role) {

        // 1. Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        // 2. Create new User object
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);

        // 3. Encrypt password
        user.setPassword(passwordEncoder.encode(rawPassword));

        // 4. Assign role
        user.setRole(role);
        user.setActive(true);

        // 5. Save to database
        return userRepository.save(user);
    }
//       FIND USER BY EMAIL
//       (Used for Login & JWT)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

//       USER VALIDATION
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

