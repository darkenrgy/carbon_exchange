package rs.example.carbon_traders.blockchain.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.example.carbon_traders.blockchain.service.BlockchainService;
import rs.example.carbon_traders.common.response.ApiResponse;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * REST endpoints for blockchain integration
 * Called by React frontend and internal Java services
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/blockchain")
public class BlockchainController {

    @Autowired
    private BlockchainService blockchainService;

    /**
     * POST /api/v1/blockchain/issue
     * Issue carbon credits after verification
     * 
     * Called after farmer data verified in MySQL
     * Creates immutable blockchain record
     */
    @PostMapping("/issue")
    public ResponseEntity<ApiResponse<String>> issueCarbonCredits(
            @RequestBody IssueCarbonRequest request
    ) {
        try {
            log.info("Issue request for farmer: {}", request.getFarmerWalletAddress());

            // Validate request
            if (request.getAmount() <= 0) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Amount must be greater than 0"));
            }

            long now = System.currentTimeMillis() / 1000;
            
            String txHash = blockchainService.issueCarbonBatch(
                request.getFarmerWalletAddress(),
                request.getAmount(),
                request.getFarmerRegistrationNo(),
                request.getLocation(),
                request.getVintageDate(),
                request.getVerificationDate() > 0 ? 
                    request.getVerificationDate() : (now - 15 * 24 * 60 * 60),
                request.getIssuanceDate() > 0 ? 
                    request.getIssuanceDate() : (now - 5 * 24 * 60 * 60),
                now,
                request.getVerificationHash()
            );

            log.info("Successfully issued batch | TxHash: {}", txHash);
            return ResponseEntity.ok(
                ApiResponse.success("Carbon batch issued on blockchain", txHash)
            );

        } catch (Exception e) {
            log.error("Error issuing carbon batch", e);
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error("Blockchain error: " + e.getMessage()));
        }
    }

    /**
     * POST /api/v1/blockchain/retire
     * Retire carbon credits when industry buyer purchases
     * Emits proof of eco-action on blockchain
     */
    @PostMapping("/retire")
    public ResponseEntity<ApiResponse<String>> retireCredits(
            @RequestBody RetireCarbonRequest request
    ) {
        try {
            log.info("Retire request for token: {} | Amount: {}", 
                request.getTokenId(), request.getAmount());

            String txHash = blockchainService.retireCarbonCredits(
                request.getBuyerAddress(),
                request.getTokenId(),
                request.getAmount(),
                request.getEcoAction()
            );

            log.info("Successfully retired credits | TxHash: {}", txHash);
            return ResponseEntity.ok(
                ApiResponse.success("Carbon credits retired and marked on blockchain", txHash)
            );

        } catch (Exception e) {
            log.error("Error retiring credits", e);
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error("Retirement error: " + e.getMessage()));
        }
    }

    /**
     * GET /api/v1/blockchain/batch/{tokenId}
     * Get complete metadata for a carbon credit batch
     * Includes all dates, farmer info, and supply status
     */
    @GetMapping("/batch/{tokenId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBatchInfo(
            @PathVariable String tokenId
    ) {
        try {
            Map<String, Object> metadata = blockchainService.getBatchMetadata(
                new BigInteger(tokenId)
            );

            return ResponseEntity.ok(
                ApiResponse.success("Batch metadata retrieved", metadata)
            );

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Invalid token ID format"));
        } catch (Exception e) {
            log.error("Error fetching batch info", e);
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error("Fetch error: " + e.getMessage()));
        }
    }

    /**
     * GET /api/v1/blockchain/batch/{tokenId}/history
     * Get all retirement records for a token
     * Proves eco-actions and industry impact
     */
    @GetMapping("/batch/{tokenId}/history")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getRetirementHistory(
            @PathVariable String tokenId
    ) {
        try {
            List<Map<String, Object>> history = blockchainService.getRetirementHistory(
                new BigInteger(tokenId)
            );

            return ResponseEntity.ok(
                ApiResponse.success("Retirement history retrieved", history)
            );

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Invalid token ID format"));
        } catch (Exception e) {
            log.error("Error fetching retirement history", e);
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error("History fetch error: " + e.getMessage()));
        }
    }

    /**
     * GET /api/v1/blockchain/health
     * Check if blockchain is available
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> checkHealth() {
        boolean available = blockchainService.isBlockchainAvailable();
        
        if (available) {
            return ResponseEntity.ok(
                ApiResponse.success("Blockchain is available", "Connected")
            );
        } else {
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error("Blockchain connection failed"));
        }
    }

    /**
     * GET /api/v1/blockchain/balance/{walletAddress}
     * Check wallet balance (for testing)
     */
    @GetMapping("/balance/{walletAddress}")
    public ResponseEntity<ApiResponse<String>> getWalletBalance(
            @PathVariable String walletAddress
    ) {
        try {
            String balance = blockchainService.getWalletBalance(walletAddress);
            return ResponseEntity.ok(
                ApiResponse.success("Wallet balance retrieved", balance + " ETH")
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error("Error fetching balance: " + e.getMessage()));
        }
    }
}

// ============ DTOs ============

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
class IssueCarbonRequest {
    private String farmerWalletAddress;    // Farmer's blockchain wallet
    private long amount;                   // Tons of CO2e
    private String farmerRegistrationNo;   // ID from registration
    private String location;               // Project location
    private long vintageDate;              // When earned (unix timestamp)
    private long verificationDate;         // When verified (unix timestamp, auto-set if 0)
    private long issuanceDate;             // When issued (unix timestamp, auto-set if 0)
    private String verificationHash;       // IPFS CID of verification docs
}

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
class RetireCarbonRequest {
    private String buyerAddress;           // Industry buyer's wallet
    private BigInteger tokenId;            // Token batch ID
    private long amount;                   // Tons to retire
    private String ecoAction;              // What eco-action was performed
}
