package rs.example.carbon_traders.farmer.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "lands")
public class Land {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    private double areaInAcre;

    private String landType; // agricultural, forest, mixed

    private String geoLocation; // lat,long

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

    public double getAreaInAcre() {
        return areaInAcre;
    }

    public void setAreaInAcre(double areaInAcre) {
        this.areaInAcre = areaInAcre;
    }

    public String getLandType() {
        return landType;
    }

    public void setLandType(String landType) {
        this.landType = landType;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }
}

