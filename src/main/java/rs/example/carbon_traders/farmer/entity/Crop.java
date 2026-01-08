package rs.example.carbon_traders.farmer.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "crops")
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    private String cropName;

    private boolean organic;

    private double estimatedCarbonCredit;

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

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public boolean isOrganic() {
        return organic;
    }

    public void setOrganic(boolean organic) {
        this.organic = organic;
    }

    public double getEstimatedCarbonCredit() {
        return estimatedCarbonCredit;
    }

    public void setEstimatedCarbonCredit(double estimatedCarbonCredit) {
        this.estimatedCarbonCredit = estimatedCarbonCredit;
    }
}

