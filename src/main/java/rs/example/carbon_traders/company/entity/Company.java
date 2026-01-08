package rs.example.carbon_traders.company.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

import rs.example.carbon_traders.user.entity.User;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String companyName;

    @Column(nullable = false, unique = true)
    private String registrationNumber;

    private String industryType;

    private Double totalCreditsPurchased = 0.0;

    private boolean verified = false;

    private LocalDateTime createdAt;

    private LocalDateTime verifiedAt;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
