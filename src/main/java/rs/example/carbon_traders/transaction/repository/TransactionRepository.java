package rs.example.carbon_traders.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.example.carbon_traders.transaction.entity.CreditTransaction;
import rs.example.carbon_traders.company.entity.Company;
import rs.example.carbon_traders.farmer.entity.Farmer;

@Repository
public interface TransactionRepository extends JpaRepository<CreditTransaction, Long> {

    List<CreditTransaction> findByCompany(Company company);

    List<CreditTransaction> findByFarmer(Farmer farmer);

    List<CreditTransaction> findByStatus(String status);
}

