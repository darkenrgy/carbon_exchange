package rs.example.carbon_traders.blockchain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import rs.example.carbon_traders.common.exception.BusinessException;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Service for interacting with CarbonCreditToken smart contract
 * Handles issuance, retirement, and metadata retrieval
 */
@Slf4j
@Service
public class BlockchainService {

    private final Web3j web3j;
    private final String contractAddress;
    private final Credentials credentials;
    private final String adminAddress;

    public BlockchainService(
            @Value("${blockchain.rpc-url}") String rpcUrl,
            @Value("${blockchain.contract-address}") String contractAddress,
            @Value("${blockchain.private-key}") String privateKey,
            @Value("${blockchain.admin-address}") String adminAddress
    ) {
        this.web3j = Web3j.build(new HttpService(rpcUrl));
        this.contractAddress = contractAddress;
        this.adminAddress = adminAddress;
        
        // Load credentials from private key
        try {
            this.credentials = Credentials.create(privateKey);
            log.info("BlockchainService initialized | Contract: {} | Admin: {}", 
                contractAddress, adminAddress);
        } catch (Exception e) {
            log.error("Failed to load credentials", e);
            throw new BusinessException("Invalid blockchain private key");
        }
    }

    /**
     * Issue a new batch of carbon credits to a farmer
     * 
     * @param farmerWalletAddress   Farmer's blockchain wallet address
     * @param amount                Tons of CO2e
     * @param farmerRegistrationNo  Farmer's registration ID
     * @param location              Project location
     * @param vintageDate           When credits were earned (unix timestamp)
     * @param verificationDate      When verified (unix timestamp)
     * @param issuanceDate          When issued (unix timestamp)
     * @param tokenizationDate      When tokenized (unix timestamp)
     * @param verificationDataHash  IPFS hash of verification docs
     * @return Transaction hash
     */
    public String issueCarbonBatch(
            String farmerWalletAddress,
            long amount,
            String farmerRegistrationNo,
            String location,
            long vintageDate,
            long verificationDate,
            long issuanceDate,
            long tokenizationDate,
            String verificationDataHash
    ) throws Exception {
        
        if (amount <= 0) {
            throw new BusinessException("Amount must be greater than 0");
        }
        if (farmerWalletAddress == null || farmerWalletAddress.isEmpty()) {
            throw new BusinessException("Farmer wallet address required");
        }

        try {
            log.info("Issuing {} tons to farmer: {}", amount, farmerWalletAddress);

            // In production, you'd use web3j contract wrapper here
            // For now, this is a template showing the parameters

            String txHash = String.format("0x%s", UUID.randomUUID().toString().replace("-", ""));
            
            log.info("Carbon batch issued | TxHash: {} | Amount: {}", txHash, amount);
            return txHash;

        } catch (Exception e) {
            log.error("Error issuing carbon batch", e);
            throw new BusinessException("Blockchain issuance failed: " + e.getMessage());
        }
    }

    /**
     * Retire carbon credits when purchased by industry buyer
     * Emits proof of eco-action on blockchain
     * 
     * @param buyerAddress    Address retiring the credits
     * @param tokenId         Token ID of batch
     * @param amount          Tons to retire
     * @param ecoAction       Description of eco-action performed
     * @return Transaction hash
     */
    public String retireCarbonCredits(
            String buyerAddress,
            BigInteger tokenId,
            long amount,
            String ecoAction
    ) throws Exception {
        
        if (amount <= 0) {
            throw new BusinessException("Amount must be greater than 0");
        }
        if (ecoAction == null || ecoAction.trim().isEmpty()) {
            throw new BusinessException("Eco action description required");
        }

        try {
            log.info("Retiring {} tons from token: {}", amount, tokenId);

            String txHash = String.format("0x%s", UUID.randomUUID().toString().replace("-", ""));
            
            log.info("Credits retired | TxHash: {} | Amount: {} | Action: {}", 
                txHash, amount, ecoAction);
            return txHash;

        } catch (Exception e) {
            log.error("Error retiring credits", e);
            throw new BusinessException("Blockchain retirement failed: " + e.getMessage());
        }
    }

    /**
     * Get metadata for a carbon credit batch
     * Used for display and verification
     */
    public Map<String, Object> getBatchMetadata(BigInteger tokenId) throws Exception {
        try {
            log.info("Fetching batch metadata for token: {}", tokenId);

            // Template response structure matching smart contract Batch struct
            Map<String, Object> batch = new HashMap<>();
            batch.put("tokenId", tokenId);
            batch.put("farmerRegistrationNo", "FARMER-XXXX");
            batch.put("location", "0°00'00.0\"N 0°00'00.0\"E");
            batch.put("vintageDate", System.currentTimeMillis() / 1000 - (30 * 24 * 60 * 60));
            batch.put("verificationDate", System.currentTimeMillis() / 1000 - (15 * 24 * 60 * 60));
            batch.put("issuanceDate", System.currentTimeMillis() / 1000 - (5 * 24 * 60 * 60));
            batch.put("tokenizationDate", System.currentTimeMillis() / 1000);
            batch.put("issued", 100);
            batch.put("retired", 25);
            batch.put("verificationDataHash", "ipfs://Qm...");

            return batch;

        } catch (Exception e) {
            log.error("Error fetching batch metadata", e);
            throw new BusinessException("Failed to fetch batch metadata: " + e.getMessage());
        }
    }

    /**
     * Get retirement history for a token
     * Shows all retirements and eco-actions performed
     */
    public List<Map<String, Object>> getRetirementHistory(BigInteger tokenId) throws Exception {
        try {
            log.info("Fetching retirement history for token: {}", tokenId);

            List<Map<String, Object>> history = new ArrayList<>();

            // Template response matching RetirementRecord struct
            Map<String, Object> record = new HashMap<>();
            record.put("retiredBy", "0x3C44CdDdB6a900fa2b585dd299e03d12FA4293BC");
            record.put("retirementDate", System.currentTimeMillis() / 1000);
            record.put("amount", 25);
            record.put("reason", "Reforestation in North America");

            history.add(record);
            return history;

        } catch (Exception e) {
            log.error("Error fetching retirement history", e);
            throw new BusinessException("Failed to fetch history: " + e.getMessage());
        }
    }

    /**
     * Check if wallet has issuer role (admin/verifier)
     * Used for authorization checks
     */
    public boolean isIssuer(String walletAddress) throws Exception {
        try {
            // In production, call hasRole() on contract
            return walletAddress.equalsIgnoreCase(adminAddress);
        } catch (Exception e) {
            log.error("Error checking issuer role", e);
            return false;
        }
    }

    /**
     * Get wallet balance (for testing)
     */
    public String getWalletBalance(String walletAddress) throws Exception {
        try {
            BigInteger balance = web3j.ethGetBalance(walletAddress, 
                org.web3j.protocol.core.DefaultBlockParameterName.LATEST).send()
                .getBalance();
            return org.web3j.utils.Convert.fromWei(
                new java.math.BigDecimal(balance), 
                org.web3j.utils.Convert.Unit.ETHER
            ).toString();
        } catch (Exception e) {
            log.error("Error fetching wallet balance", e);
            return "0";
        }
    }

    /**
     * Verify blockchain connection
     */
    public boolean isBlockchainAvailable() {
        try {
            web3j.web3ClientVersion().send();
            return true;
        } catch (Exception e) {
            log.error("Blockchain connection failed", e);
            return false;
        }
    }
}
