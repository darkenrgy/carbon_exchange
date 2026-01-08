package rs.example.carbon_traders.company.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rs.example.carbon_traders.company.entity.Company;
import rs.example.carbon_traders.company.service.CompanyService;
import rs.example.carbon_traders.user.entity.User;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/create")
    public ResponseEntity<Company> createCompany(
            @RequestBody Company company,
            @RequestAttribute("user") User user
    ) {
        return ResponseEntity.ok(
                companyService.createCompanyProfile(company, user)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<Company> getMyCompany(
            @RequestAttribute("user") User user
    ) {
        return ResponseEntity.ok(
                companyService.getCompanyByUser(user)
        );
    }

    @GetMapping("/all")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }
}

