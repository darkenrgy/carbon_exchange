package rs.example.carbon_traders.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import rs.example.carbon_traders.ai.service.AIVerificationService;
import rs.example.carbon_traders.common.response.ApiResponse;

@RestController
@RequestMapping("/api/ai")
public class AIVerificationController {

    private final AIVerificationService aiVerificationService;

    public AIVerificationController(AIVerificationService aiVerificationService) {
        this.aiVerificationService = aiVerificationService;
    }

    @PostMapping("/verify-crop")
    public ResponseEntity<ApiResponse<String>> verifyCrop(
            @RequestParam MultipartFile image,
            @RequestParam String cropType) {

        String result = aiVerificationService.verifyCrop(image, cropType);

        return ResponseEntity.ok(
                ApiResponse.success("AI verification completed", result)
        );
    }
}

