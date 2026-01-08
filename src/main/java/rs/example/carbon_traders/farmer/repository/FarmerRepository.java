package rs.example.carbon_traders.farmer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.example.carbon_traders.farmer.entity.Farmer;
import rs.example.carbon_traders.user.entity.User;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {

    // Find farmer profile using User
    Optional<Farmer> findByUser(User user);

    // Check if farmer profile already exists for a user
    boolean existsByUser(User user);
}

