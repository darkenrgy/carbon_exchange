package rs.example.carbon_traders.transaction.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.example.carbon_traders.transaction.entity.CreditTransaction;
import rs.example.carbon_traders.transaction.repository.TransactionRepository;
import rs.example.carbon_traders.carboncredit.entity.CarbonCredit;
import rs.example.carbon_traders.carboncredit.service.CarbonCreditService;
import rs.example.carbon_traders.company.entity.Company;
import rs.example.carbon_traders.farmer.entity.Farmer;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CarbonCreditService carbonCreditService;

    public TransactionService(TransactionRepository transactionRepository,
                              CarbonCreditService carbonCreditService) {
        this.transactionRepository = transactionRepository;
        this.carbonCreditService = carbonCreditService;
    }

    // buy cradit

    public CreditTransaction buyCarbonCredits(
            Company company,
            Farmer farmer,
            CarbonCredit carbonCredit,
            Double creditsToBuy) {

        Double totalAmount = creditsToBuy * carbonCredit.getPricePerCredit();

        // Step 1: Update carbon credit balance
        carbonCreditService.buyCredits(
                carbonCredit.getId(),
                company,
                creditsToBuy
        );
        // record
        CreditTransaction transaction = CreditTransaction.builder()
                .company(company)
                .farmer(farmer)
                .carbonCredit(carbonCredit)
                .creditsPurchased(creditsToBuy)
                .pricePerCredit(carbonCredit.getPricePerCredit())
                .totalAmount(totalAmount)
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .build();

        return transactionRepository.save(transaction);
    }
    // fetxh

    public List<CreditTransaction> getTransactionsByCompany(Company company) {
        return transactionRepository.findByCompany(company);
    }

    public List<CreditTransaction> getTransactionsByFarmer(Farmer farmer) {
        return transactionRepository.findByFarmer(farmer);
    }

    public List<CreditTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}

