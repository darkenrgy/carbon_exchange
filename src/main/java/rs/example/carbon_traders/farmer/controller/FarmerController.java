package rs.example.carbon_traders.farmer.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rs.example.carbon_traders.farmer.entity.Crop;
import rs.example.carbon_traders.farmer.entity.Farmer;
import rs.example.carbon_traders.farmer.entity.Land;
import rs.example.carbon_traders.farmer.entity.SoilReport;
import rs.example.carbon_traders.farmer.service.FarmerService;

@RestController
@RequestMapping("/api/farmer")
@CrossOrigin
public class FarmerController {

    private final FarmerService farmerService;

    public FarmerController(FarmerService farmerService) {
        this.farmerService = farmerService;
    }

//       CREATE FARMER PROFILE
    @PostMapping("/profile")
    public ResponseEntity<Farmer> createProfile(
            @RequestBody Farmer farmer,
            Principal principal) {

        Farmer savedFarmer = farmerService.createFarmerProfile(
                farmer, principal.getName());

        return ResponseEntity.ok(savedFarmer);
    }

//       GET MY FARMER PROFILE
    @GetMapping("/profile")
    public ResponseEntity<Farmer> getMyProfile(Principal principal) {

        Farmer farmer = farmerService.getFarmerByEmail(
                principal.getName());

        return ResponseEntity.ok(farmer);
    }
//       ADD LAND DETAILS
    @PostMapping("/land")
    public ResponseEntity<Land> addLand(
            @RequestBody Land land,
            Principal principal) {

        Land savedLand = farmerService.addLand(
                land, principal.getName());

        return ResponseEntity.ok(savedLand);
    }

//       ADD CROP DETAILS
    @PostMapping("/crop")
    public ResponseEntity<Crop> addCrop(
            @RequestBody Crop crop,
            Principal principal) {

        Crop savedCrop = farmerService.addCrop(
                crop, principal.getName());

        return ResponseEntity.ok(savedCrop);
    }


//       ADD SOIL REPORT

    @PostMapping("/soil-report")
    public ResponseEntity<SoilReport> addSoilReport(
            @RequestBody SoilReport soilReport,
            Principal principal) {

        SoilReport savedReport = farmerService.addSoilReport(
                soilReport, principal.getName());

        return ResponseEntity.ok(savedReport);
    }

//       VIEW MY CROPS
    @GetMapping("/crops")
    public ResponseEntity<List<Crop>> getMyCrops(
            Principal principal) {

        return ResponseEntity.ok(
                farmerService.getCrops(principal.getName()));
    }

//       VIEW MY LANDS
    @GetMapping("/lands")
    public ResponseEntity<List<Land>> getMyLands(
            Principal principal) {

        return ResponseEntity.ok(
                farmerService.getLands(principal.getName()));
    }

//       VIEW MY SOIL REPORTS
    @GetMapping("/soil-reports")
    public ResponseEntity<List<SoilReport>> getMySoilReports(
            Principal principal) {

        return ResponseEntity.ok(
                farmerService.getSoilReports(principal.getName()));
    }
}

