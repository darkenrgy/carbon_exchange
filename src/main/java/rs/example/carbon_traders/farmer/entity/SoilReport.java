package rs.example.carbon_traders.farmer.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "soil_reports")
public class SoilReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    private String reportNumber;

    private String issuedBy; // Govt / Lab name

    private String carbonContentLevel;

    /* Getters & Setters */

    public Long getId() {
        return id;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public String getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(String reportNumber) {
        this.reportNumber = reportNumber;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getCarbonContentLevel() {
        return carbonContentLevel;
    }

    public void setCarbonContentLevel(String carbonContentLevel) {
        this.carbonContentLevel = carbonContentLevel;
    }
}

