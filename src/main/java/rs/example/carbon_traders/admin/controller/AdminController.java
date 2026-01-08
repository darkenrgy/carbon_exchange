package rs.example.carbon_traders.admin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rs.example.carbon_traders.admin.service.AdminService;
import rs.example.carbon_traders.farmer.entity.Farmer;
import rs.example.carbon_traders.company.entity.Company;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // farmer

    @GetMapping("/farmers")
    public ResponseEntity<List<Farmer>> getAllFarmers() {
        return ResponseEntity.ok(adminService.getAllFarmers());
    }

    @PutMapping("/farmer/{id}/verify")
    public ResponseEntity<Farmer> verifyFarmer(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.verifyFarmer(id));
    }

    @PutMapping("/farmer/{id}/reject")
    public ResponseEntity<Farmer> rejectFarmer(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.rejectFarmer(id));
    }

    // company

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(adminService.getAllCompanies());
    }

    @PutMapping("/company/{id}/verify")
    public ResponseEntity<Company> verifyCompany(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.verifyCompany(id));
    }

    // dashboad

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        return ResponseEntity.ok(adminService.getPlatformStats());
    }
}


