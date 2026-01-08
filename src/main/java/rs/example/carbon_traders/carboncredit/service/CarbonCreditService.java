package rs.example.carbon_traders.carboncredit.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.example.carbon_traders.carboncredit.entity.CarbonCredit;
import rs.example.carbon_traders.carboncredit.repository.CarbonCreditRepository;
import rs.example.carbon_traders.company.entity.Company;
import rs.example.carbon_traders.farmer.entity.Farmer;

@Service
@Transactional
public class CarbonCreditService {

    private final CarbonCreditRepository carbonCreditRepository;

    public CarbonCreditService(CarbonCreditRepository carbonCreditRepository) {
        this.carbonCreditRepository = carbonCreditRepository;
    }

    //farmer generated cradit

    public CarbonCredit generateCredits(
            Farmer farmer,
            Double credits,
            Double pricePerCredit) {

        CarbonCredit carbonCredit = CarbonCredit.builder()
                .farmer(farmer)
                .totalCredits(credits)
                .availableCredits(credits)
                .pricePerCredit(pricePerCredit)
                .status("GENERATED")
                .createdAt(LocalDateTime.now())
                .build();

        return carbonCreditRepository.save(carbonCredit);
    }

    // company buy cradit

    public CarbonCredit buyCredits(
            Long creditId,
            Company company,
            Double creditsToBuy) {

        CarbonCredit credit = carbonCreditRepository.findById(creditId)
                .orElseThrow(() -> new RuntimeException("Carbon credit not found"));

        if (credit.getAvailableCredits() < creditsToBuy) {
            throw new RuntimeException("Not enough credits available");
        }

        // Update credit balances
        credit.setAvailableCredits(
                credit.getAvailableCredits() - creditsToBuy
        );

        credit.setSoldCredits(
                credit.getSoldCredits() + creditsToBuy
        );

        credit.setCompany(company);
        credit.setUpdatedAt(LocalDateTime.now());

        if (credit.getAvailableCredits() == 0) {
            credit.setStatus("SOLD");
        } else {
            credit.setStatus("PARTIALLY_SOLD");
        }

        return carbonCreditRepository.save(credit);
    }

    //featching detail

    public List<CarbonCredit> getCreditsByFarmer(Farmer farmer) {
        return carbonCreditRepository.findByFarmer(farmer);
    }

    public List<CarbonCredit> getCreditsByCompany(Company company) {
        return carbonCreditRepository.findByCompany(company);
    }

    public List<CarbonCredit> getAvailableCredits() {
        return carbonCreditRepository.findByStatus("GENERATED");
    }
}

