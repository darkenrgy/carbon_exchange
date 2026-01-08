package rs.example.carbon_traders.carboncredit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.example.carbon_traders.carboncredit.entity.CarbonCredit;
import rs.example.carbon_traders.farmer.entity.Farmer;
import rs.example.carbon_traders.company.entity.Company;

@Repository
public interface CarbonCreditRepository extends JpaRepository<CarbonCredit, Long> {

    List<CarbonCredit> findByFarmer(Farmer farmer);

    List<CarbonCredit> findByCompany(Company company);

    List<CarbonCredit> findByStatus(String status);
}

