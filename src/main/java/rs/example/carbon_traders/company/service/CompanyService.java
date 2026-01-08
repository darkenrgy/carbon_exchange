package rs.example.carbon_traders.company.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.example.carbon_traders.company.entity.Company;
import rs.example.carbon_traders.company.repository.CompanyRepository;
import rs.example.carbon_traders.user.entity.User;

@Service
@Transactional
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company createCompanyProfile(Company company, User user) {

        if (companyRepository.existsByUser(user)) {
            throw new RuntimeException("Company already registered for this user");
        }

        if (companyRepository.existsByRegistrationNumber(company.getRegistrationNumber())) {
            throw new RuntimeException("Company registration number already exists");
        }

        company.setUser(user);
        company.setCreatedAt(LocalDateTime.now());
        company.setVerified(false);

        return companyRepository.save(company);
    }

    public Company getCompanyByUser(User user) {
        return companyRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Company profile not found"));
    }

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

    public void addPurchasedCredits(Long companyId, Double credits) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        company.setTotalCreditsPurchased(
                company.getTotalCreditsPurchased() + credits
        );

        companyRepository.save(company);
    }
}

