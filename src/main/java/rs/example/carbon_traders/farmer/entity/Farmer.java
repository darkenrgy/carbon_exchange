package rs.example.carbon_traders.farmer.entity;

import jakarta.persistence.*;

import rs.example.carbon_traders.user.entity.User;

@Entity
@Table(name = "farmers")
public class Farmer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One farmer = one user account
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String village;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String state;

    private boolean verified = false;

    private String verificationRemark;

//       Getters & Setters

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getVerificationRemark() {
        return verificationRemark;
    }

    public void setVerificationRemark(String verificationRemark) {
        this.verificationRemark = verificationRemark;
    }
}

