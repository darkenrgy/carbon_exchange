package rs.example.carbon_traders.admin.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.example.carbon_traders.farmer.entity.Farmer;
import rs.example.carbon_traders.farmer.repository.FarmerRepository;
import rs.example.carbon_traders.company.entity.Company;
import rs.example.carbon_traders.company.repository.CompanyRepository;
import rs.example.carbon_traders.user.repository.UserRepository;

@Service
@Transactional
public class AdminService {

    private final FarmerRepository farmerRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public AdminService(FarmerRepository farmerRepository,
                        CompanyRepository companyRepository,
                        UserRepository userRepository) {
        this.farmerRepository = farmerRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    //FARMER

    public List<Farmer> getAllFarmers() {
        return farmerRepository.findAll();
    }

    public Farmer verifyFarmer(Long farmerId) {
        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));

        farmer.setVerified(true);
        farmer.setVerifiedAt(LocalDateTime.now());

        return farmerRepository.save(farmer);
    }

    public Farmer rejectFarmer(Long farmerId) {
        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));

        farmer.setVerified(false);
        return farmerRepository.save(farmer);
    }

    // COMPANY

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company verifyCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        company.setVerified(true);
        company.setVerifiedAt(LocalDateTime.now());

        return companyRepository.save(company);
    }

    // DASHBOARD STATS

    public Map<String, Object> getPlatformStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalUsers", userRepository.count());
        stats.put("totalFarmers", farmerRepository.count());
        stats.put("totalCompanies", companyRepository.count());

        stats.put("verifiedFarmers",
                farmerRepository.countByVerified(true));

        stats.put("verifiedCompanies",
                companyRepository.countByVerified(true));

        stats.put("totalCreditsConsumed",
                companyRepository.findAll()
                        .stream()
                        .mapToDouble(Company::getTotalCreditsPurchased)
                        .sum()
        );

        return stats;
    }
}
