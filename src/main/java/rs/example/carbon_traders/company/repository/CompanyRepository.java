package rs.example.carbon_traders.company.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.example.carbon_traders.company.entity.Company;
import rs.example.carbon_traders.user.entity.User;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByUser(User user);

    boolean existsByUser(User user);

    boolean existsByRegistrationNumber(String registrationNumber);
}

