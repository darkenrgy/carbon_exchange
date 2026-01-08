package rs.example.carbon_traders.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.example.carbon_traders.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (used for login & JWT)
    Optional<User> findByEmail(String email);

    // Check if email already exists (used during registration)
    boolean existsByEmail(String email);
}

