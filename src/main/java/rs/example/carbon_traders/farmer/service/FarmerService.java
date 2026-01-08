package rs.example.carbon_traders.farmer.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.example.carbon_traders.farmer.entity.Farmer;
import rs.example.carbon_traders.farmer.repository.FarmerRepository;
import rs.example.carbon_traders.user.entity.User;

@Service
@Transactional
public class FarmerService {

    private final FarmerRepository farmerRepository;

    public FarmerService(FarmerRepository farmerRepository) {
        this.farmerRepository = farmerRepository;
    }

//     * Create farmer profile (one farmer per user)
    public Farmer createFarmerProfile(Farmer farmer, User user) {

        if (farmerRepository.existsByUser(user)) {
            throw new RuntimeException("Farmer profile already exists for this user");
        }

        farmer.setUser(user);
        farmer.setCreatedAt(LocalDateTime.now());
        farmer.setVerified(false);

        return farmerRepository.save(farmer);
    }

//     * Get farmer profile by user
    public Farmer getFarmerByUser(User user) {
        return farmerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Farmer profile not found"));
    }
//     * Get farmer by ID (admin / verification)
    public Farmer getFarmerById(Long farmerId) {
        return farmerRepository.findById(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));
    }
//     * Get all farmers (admin dashboard)
    public List<Farmer> getAllFarmers() {
        return farmerRepository.findAll();
    }

//     * Verify farmer (admin authority)
    public Farmer verifyFarmer(Long farmerId) {
        Farmer farmer = getFarmerById(farmerId);
        farmer.setVerified(true);
        farmer.setVerifiedAt(LocalDateTime.now());
        return farmerRepository.save(farmer);
    }

//     * Reject farmer verification
    public Farmer rejectFarmer(Long farmerId, String reason) {
        Farmer farmer = getFarmerById(farmerId);
        farmer.setVerified(false);
        farmer.setRejectionReason(reason);
        return farmerRepository.save(farmer);
    }
}

