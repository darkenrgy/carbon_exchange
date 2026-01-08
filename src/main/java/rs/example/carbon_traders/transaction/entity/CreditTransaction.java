package rs.example.carbon_traders.transaction.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

import rs.example.carbon_traders.company.entity.Company;
import rs.example.carbon_traders.farmer.entity.Farmer;
import rs.example.carbon_traders.carboncredit.entity.CarbonCredit;

@Entity
@Table(name = "credit_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Buyer
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    // Seller
    @ManyToOne
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;
    @ManyToOne
    @JoinColumn(name = "carbon_credit_id", nullable = false)
    private CarbonCredit carbonCredit;
    private Double creditsPurchased;
    private Double pricePerCredit;
    private Double totalAmount;
    private String status;

    // For future blockchain
    private String blockchainTxHash;

    private LocalDateTime createdAt;
}

