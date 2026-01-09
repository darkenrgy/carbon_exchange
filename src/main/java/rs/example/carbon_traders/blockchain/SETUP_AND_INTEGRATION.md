# Carbon Credit Blockchain Integration Guide

Complete setup guide for integrating the blockchain smart contracts with Java backend, React frontend, and MySQL database.

## 📦 Quick Setup (New Team Member)

### 1. Extract & Install
```bash
# Extract blockchain-carbon-traders.zip
unzip blockchain-carbon-traders.zip
cd blockchain

# Install dependencies
npm install
```

### 2. Configure Environment
```bash
# Copy example to actual .env
cp .env.example .env

# Edit .env with your values
# For LOCAL TESTING (no blockchain needed yet):
BASE_URI="https://example.com/metadata/"
ADMIN_ADDRESS=""  # Leave empty to use deployer as admin
```

### 3. Run Local Tests (No External RPC needed)
```bash
# Compile smart contract
npm run compile

# Run tests
npm test

# Run automated demo (issue + retire credits)
npm run auto:local
```

---

## 🔗 Integration with Java Backend

### Step 1: Add Web3j Dependency (Maven)
Add to `pom.xml`:
```xml
<dependency>
    <groupId>org.web3j</groupId>
    <artifactId>core</artifactId>
    <version>4.9.7</version>
</dependency>
<dependency>
    <groupId>org.web3j</groupId>
    <artifactId>contracts</artifactId>
    <version>4.9.7</version>
</dependency>
```

### Step 2: Generate Java Contract Bindings
From blockchain folder, generate Java code from contract ABI:
```bash
# First, compile contract to get ABI
npm run compile

# Get ABI from artifacts
cat artifacts/contracts/CarbonCreditToken.sol/CarbonCreditToken.json | jq '.abi' > ../src/main/resources/CarbonCreditToken.abi

# Generate Java wrapper (requires web3j CLI)
# Download: https://docs.web3j.io/command_line_tools
web3j generate solidity -a artifacts/contracts/CarbonCreditToken.sol/CarbonCreditToken.json \
  -b artifacts/contracts/CarbonCreditToken.sol/CarbonCreditToken.bin \
  -o ../src/main/java -p rs.example.carbon_traders.blockchain.web3
```

### Step 3: Create Java Service Layer
Create file: `src/main/java/rs/example/carbon_traders/blockchain/service/BlockchainService.java`

```java
package rs.example.carbon_traders.blockchain.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import rs.example.carbon_traders.blockchain.web3.CarbonCreditToken;
import java.math.BigInteger;
import java.time.Instant;

@Service
public class BlockchainService {

    private final Web3j web3j;
    private final String contractAddress;
    private final String privateKey;
    private final String adminAddress;

    public BlockchainService(
        @Value("${blockchain.rpc-url}") String rpcUrl,
        @Value("${blockchain.contract-address}") String contractAddress,
        @Value("${blockchain.private-key}") String privateKey,
        @Value("${blockchain.admin-address}") String adminAddress
    ) {
        this.web3j = Web3j.build(new HttpService(rpcUrl));
        this.contractAddress = contractAddress;
        this.privateKey = privateKey;
        this.adminAddress = adminAddress;
    }

    /**
     * Issue a new batch of carbon credits from verified farmer data
     */
    public String issueCarbonBatch(
        String farmerAddress,
        long amount,
        String farmerRegistrationNo,
        String location,
        long vintageDate,
        long verificationDate,
        long issuanceDate,
        long tokenizationDate,
        String verificationDataHash
    ) throws Exception {
        // Load contract
        CarbonCreditToken contract = loadContract();

        // Create transaction
        var txHash = contract.issueBatch(
            farmerAddress,
            BigInteger.valueOf(amount),
            farmerRegistrationNo,
            location,
            BigInteger.valueOf(vintageDate),
            BigInteger.valueOf(verificationDate),
            BigInteger.valueOf(issuanceDate),
            BigInteger.valueOf(tokenizationDate),
            verificationDataHash
        ).sendAsync().get().getTransactionHash();

        return txHash;
    }

    /**
     * Retire carbon credits (called by industry buyer)
     */
    public String retireCarbonCredits(
        String buyerAddress,
        BigInteger tokenId,
        long amount,
        String ecoAction
    ) throws Exception {
        CarbonCreditToken contract = loadContract();

        var txHash = contract.retire(
            tokenId,
            BigInteger.valueOf(amount),
            ecoAction
        ).sendAsync().get().getTransactionHash();

        return txHash;
    }

    /**
     * Get batch metadata
     */
    public CarbonCreditToken.Batch getBatchMetadata(BigInteger tokenId) throws Exception {
        CarbonCreditToken contract = loadContract();
        return contract.getBatch(tokenId).sendAsync().get();
    }

    /**
     * Get retirement history for a token
     */
    public java.util.List<CarbonCreditToken.RetirementRecord> getRetirementHistory(
        BigInteger tokenId
    ) throws Exception {
        CarbonCreditToken contract = loadContract();
        return contract.getRetirementHistory(tokenId).sendAsync().get();
    }

    private CarbonCreditToken loadContract() throws Exception {
        // In production, load credentials from secure vault
        var credentials = org.web3j.crypto.Credentials.create(privateKey);
        return CarbonCreditToken.load(
            contractAddress,
            web3j,
            credentials,
            new DefaultGasProvider()
        );
    }
}
```

### Step 4: Create REST Controller
Create file: `src/main/java/rs/example/carbon_traders/blockchain/controller/BlockchainController.java`

```java
package rs.example.carbon_traders.blockchain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rs.example.carbon_traders.blockchain.service.BlockchainService;
import rs.example.carbon_traders.carboncredit.entity.CarbonCredit;
import rs.example.carbon_traders.common.response.ApiResponse;
import java.math.BigInteger;

@RestController
@RequestMapping("/api/v1/blockchain")
public class BlockchainController {

    @Autowired
    private BlockchainService blockchainService;

    /**
     * Issue carbon credits after verification
     * Called after farmer data is verified in DB
     */
    @PostMapping("/issue")
    public ApiResponse<String> issueCarbonCredits(
        @RequestBody IssueRequest request
    ) {
        try {
            long now = System.currentTimeMillis() / 1000;
            String txHash = blockchainService.issueCarbonBatch(
                request.getFarmerWalletAddress(),
                request.getAmount(),
                request.getFarmerRegistrationNo(),
                request.getLocation(),
                request.getVintageDate(),
                now - (15 * 24 * 60 * 60),  // verified 15 days ago
                now - (5 * 24 * 60 * 60),   // issued 5 days ago
                now,                         // tokenized now
                request.getVerificationHash()
            );
            return ApiResponse.success("Carbon batch issued", txHash);
        } catch (Exception e) {
            return ApiResponse.error("Blockchain error: " + e.getMessage());
        }
    }

    /**
     * Retire credits when industry buys and performs eco action
     */
    @PostMapping("/retire")
    public ApiResponse<String> retireCredits(
        @RequestBody RetireRequest request
    ) {
        try {
            String txHash = blockchainService.retireCarbonCredits(
                request.getBuyerAddress(),
                request.getTokenId(),
                request.getAmount(),
                request.getEcoAction()
            );
            return ApiResponse.success("Credits retired", txHash);
        } catch (Exception e) {
            return ApiResponse.error("Retirement error: " + e.getMessage());
        }
    }

    /**
     * Get batch metadata (for verification display)
     */
    @GetMapping("/batch/{tokenId}")
    public ApiResponse<?> getBatchInfo(@PathVariable String tokenId) {
        try {
            var metadata = blockchainService.getBatchMetadata(new BigInteger(tokenId));
            return ApiResponse.success("Batch metadata", metadata);
        } catch (Exception e) {
            return ApiResponse.error("Fetch error: " + e.getMessage());
        }
    }

    /**
     * Get retirement history (proof of eco action)
     */
    @GetMapping("/batch/{tokenId}/history")
    public ApiResponse<?> getRetirementHistory(@PathVariable String tokenId) {
        try {
            var history = blockchainService.getRetirementHistory(new BigInteger(tokenId));
            return ApiResponse.success("Retirement history", history);
        } catch (Exception e) {
            return ApiResponse.error("History fetch error: " + e.getMessage());
        }
    }
}

// DTOs
@lombok.Data
class IssueRequest {
    private String farmerWalletAddress;
    private long amount;
    private String farmerRegistrationNo;
    private String location;
    private long vintageDate;
    private String verificationHash;
}

@lombok.Data
class RetireRequest {
    private String buyerAddress;
    private BigInteger tokenId;
    private long amount;
    private String ecoAction;
}
```

### Step 5: Add Configuration (application.yml)
```yaml
blockchain:
  rpc-url: "http://localhost:8545"  # Or Sepolia: https://sepolia.infura.io/v3/YOUR_KEY
  contract-address: "0x5FbDB2315678afecb367f032d93F642f64180aa3"
  private-key: "0x..."  # Admin/issuer private key
  admin-address: "0x..."  # Admin wallet address
```

### Step 6: Wire Into Existing Services
In your `CarbonCreditService.java` (existing), call blockchain when verification completes:

```java
@Autowired
private BlockchainService blockchainService;

@Transactional
public void verifyCarbonCredit(Long creditId, String farmerWalletAddress) {
    // 1. Mark as verified in MySQL
    CarbonCredit credit = carbonCreditRepository.findById(creditId);
    credit.setStatus("VERIFIED");
    credit.setVerificationDate(LocalDateTime.now());
    carbonCreditRepository.save(credit);

    // 2. Issue on blockchain (creates immutable record)
    try {
        String txHash = blockchainService.issueCarbonBatch(
            farmerWalletAddress,
            credit.getAmount(),
            credit.getFarmerRegistrationNo(),
            credit.getLocation(),
            credit.getVintageDate().getTime() / 1000,
            System.currentTimeMillis() / 1000,
            System.currentTimeMillis() / 1000,
            System.currentTimeMillis() / 1000,
            credit.getVerificationDataHash()
        );

        // 3. Store blockchain reference in MySQL
        credit.setBlockchainTxHash(txHash);
        carbonCreditRepository.save(credit);
    } catch (Exception e) {
        throw new RuntimeException("Blockchain issuance failed", e);
    }
}
```

---

## ⚛️ Integration with React Frontend

### Step 1: Install Web3 Libraries
```bash
cd frontend
npm install ethers react-toastify axios
```

### Step 2: Create Web3 Context
Create: `src/context/BlockchainContext.jsx`

```jsx
import React, { createContext, useState, useCallback } from 'react';
import { ethers } from 'ethers';

export const BlockchainContext = createContext();

export const BlockchainProvider = ({ children }) => {
  const [account, setAccount] = useState(null);
  const [provider, setProvider] = useState(null);
  const [balance, setBalance] = useState(0);
  const [error, setError] = useState(null);

  const connectWallet = useCallback(async () => {
    try {
      const accounts = await window.ethereum.request({
        method: 'eth_requestAccounts',
      });
      
      const provider = new ethers.BrowserProvider(window.ethereum);
      const balance = await provider.getBalance(accounts[0]);
      
      setAccount(accounts[0]);
      setProvider(provider);
      setBalance(ethers.formatEther(balance));
      setError(null);
    } catch (err) {
      setError(err.message);
    }
  }, []);

  return (
    <BlockchainContext.Provider value={{ account, provider, balance, error, connectWallet }}>
      {children}
    </BlockchainContext.Provider>
  );
};
```

### Step 3: Create Carbon Credit Component
Create: `src/components/CarbonCreditIssuance.jsx`

```jsx
import React, { useState, useContext } from 'react';
import { BlockchainContext } from '../context/BlockchainContext';
import axios from 'axios';
import { toast } from 'react-toastify';

export const CarbonCreditIssuance = () => {
  const { account } = useContext(BlockchainContext);
  const [formData, setFormData] = useState({
    farmerRegistrationNo: '',
    location: '',
    amount: 0,
    verificationHash: ''
  });
  const [txHash, setTxHash] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      // Call Java backend to issue blockchain credits
      const response = await axios.post('/api/v1/blockchain/issue', {
        farmerWalletAddress: account,
        farmerRegistrationNo: formData.farmerRegistrationNo,
        location: formData.location,
        amount: parseInt(formData.amount),
        vintageDate: Math.floor(Date.now() / 1000) - (30 * 24 * 60 * 60),
        verificationHash: formData.verificationHash
      });

      setTxHash(response.data.data);
      toast.success('Carbon credits issued!');
    } catch (error) {
      toast.error('Error: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="carbon-issuance-form">
      <h2>Issue Carbon Credits</h2>
      {account && <p>Connected: {account}</p>}
      
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="farmerRegistrationNo"
          placeholder="Farmer Registration No."
          value={formData.farmerRegistrationNo}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="location"
          placeholder="Location (GPS)"
          value={formData.location}
          onChange={handleChange}
          required
        />
        <input
          type="number"
          name="amount"
          placeholder="Amount (tons CO2e)"
          value={formData.amount}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="verificationHash"
          placeholder="IPFS Verification Hash"
          value={formData.verificationHash}
          onChange={handleChange}
        />
        <button type="submit" disabled={loading}>
          {loading ? 'Issuing...' : 'Issue Credits'}
        </button>
      </form>

      {txHash && (
        <div className="success-message">
          <p>✅ Credits Issued!</p>
          <p>Tx Hash: <code>{txHash}</code></p>
        </div>
      )}
    </div>
  );
};
```

### Step 4: Display Carbon Credit Details
Create: `src/components/CarbonCreditDetails.jsx`

```jsx
import React, { useState } from 'react';
import axios from 'axios';

export const CarbonCreditDetails = ({ tokenId }) => {
  const [batch, setBatch] = useState(null);
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchDetails = async () => {
    setLoading(true);
    try {
      const batchRes = await axios.get(`/api/v1/blockchain/batch/${tokenId}`);
      const historyRes = await axios.get(`/api/v1/blockchain/batch/${tokenId}/history`);
      
      setBatch(batchRes.data.data);
      setHistory(historyRes.data.data);
    } catch (error) {
      console.error('Error fetching details:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="carbon-details">
      <button onClick={fetchDetails} disabled={loading}>
        {loading ? 'Loading...' : 'Load Details'}
      </button>

      {batch && (
        <div className="batch-info">
          <h3>Batch #{tokenId}</h3>
          <p><strong>Farmer:</strong> {batch.farmerRegistrationNo}</p>
          <p><strong>Location:</strong> {batch.location}</p>
          <p><strong>Issued:</strong> {batch.issued} tons CO2e</p>
          <p><strong>Retired:</strong> {batch.retired} tons CO2e</p>
          <p><strong>Vintage Date:</strong> {new Date(batch.vintageDate * 1000).toLocaleDateString()}</p>
          <p><strong>Verification Date:</strong> {new Date(batch.verificationDate * 1000).toLocaleDateString()}</p>
        </div>
      )}

      {history.length > 0 && (
        <div className="retirement-history">
          <h4>Retirement History</h4>
          {history.map((record, i) => (
            <div key={i} className="retirement-record">
              <p><strong>Amount Retired:</strong> {record.amount}</p>
              <p><strong>Date:</strong> {new Date(record.retirementDate * 1000).toLocaleDateString()}</p>
              <p><strong>Eco Action:</strong> {record.reason}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
```

---

## 🗄️ MySQL Database Schema

Add these tables to track blockchain data:

```sql
-- Store reference to blockchain emissions
ALTER TABLE carbon_credit ADD COLUMN blockchain_token_id BIGINT;
ALTER TABLE carbon_credit ADD COLUMN blockchain_tx_hash VARCHAR(255);
ALTER TABLE carbon_credit ADD COLUMN blockchain_issued_date TIMESTAMP;

-- Track retirement transactions
CREATE TABLE carbon_retirement (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  token_id BIGINT NOT NULL,
  buyer_id BIGINT NOT NULL,
  amount BIGINT NOT NULL,
  eco_action VARCHAR(500),
  blockchain_tx_hash VARCHAR(255),
  retirement_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (buyer_id) REFERENCES user(id)
);

-- Cache blockchain metadata (for faster queries)
CREATE TABLE blockchain_batch_cache (
  token_id BIGINT PRIMARY KEY,
  farmer_registration_no VARCHAR(255),
  location VARCHAR(255),
  vintage_date BIGINT,
  verification_date BIGINT,
  issuance_date BIGINT,
  tokenization_date BIGINT,
  issued_amount BIGINT,
  retired_amount BIGINT,
  verification_hash VARCHAR(255),
  cached_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (token_id) REFERENCES carbon_credit(id)
);
```

---

## 🚀 Complete Project Setup Steps

### For Your Friend (First Time Setup)

1. **Extract Blockchain**
   ```bash
   unzip blockchain-carbon-traders.zip
   cd blockchain
   npm install
   npm run auto:local  # Test it works
   ```

2. **Copy Contract ABI to Java Project**
   ```bash
   cp artifacts/contracts/CarbonCreditToken.sol/CarbonCreditToken.json \
      ../src/main/resources/
   ```

3. **Add Java Files** (from Step 3-5 above)
   - Copy `BlockchainService.java`
   - Copy `BlockchainController.java`
   - Add dependencies to `pom.xml`
   - Update `application.yml` with blockchain config

4. **Add React Components** (from Step 2-4 above)
   - Copy context, components to React project
   - Install web3 libraries
   - Import in main App

5. **Add Database Tables** (from MySQL section)

6. **Deploy Contract** (When ready for production)
   ```bash
   # For testnet (Sepolia)
   cp .env.example .env
   # Fill in RPC_URL, PRIVATE_KEY, etc.
   npm run deploy:sepolia
   # Update Java config with deployed contract address
   ```

---

## 📧 Sharing Instructions

**Create a ZIP with everything:**
```bash
# From parent directory
zip -r blockchain-carbon-traders.zip blockchain/ \
  -x "blockchain/node_modules/*" "blockchain/.env" "blockchain/artifacts/*"

# Share via email with README
```

**Include in ZIP:**
- ✅ All contract files
- ✅ Scripts & tests
- ✅ .env.example (NO .env!)
- ✅ package.json
- ✅ hardhat.config.js
- ✅ This setup guide (SETUP.md)

---

## 🔑 Key Integration Points

| Component | Purpose | Integration |
|-----------|---------|-------------|
| **Blockchain** | Immutable ledger of carbon credits | Issues tokens when verified |
| **Java Backend** | Verification & business logic | Calls blockchain via Web3j |
| **MySQL** | User data, transactions | Stores references + metadata |
| **React Frontend** | Web UI & wallet connection | Displays credits & retirement history |

**Data Flow:**
```
Farmer Registration (MySQL) 
  → Verification (Java) 
  → Issue on Blockchain (Smart Contract) 
  → Store TX Hash (MySQL) 
  → Display in React (Frontend)
  → Industry buys → Retire Credits (Blockchain) → Proof (History)
```

---

## ❓ Common Questions

**Q: Do we need a full Ethereum node?**
A: No, use public RPC (Infura/Alchemy) for Sepolia testnet.

**Q: Can we use it without wallet for testing?**
A: Yes, Java backend controls private key—no frontend wallet required.

**Q: How to migrate from testnet to mainnet?**
A: Deploy to mainnet, update contract address in Java config.

**Q: How does verification data get to blockchain?**
A: IPFS hash (stored in `verificationDataHash`) links to detailed docs off-chain.

All files ready! Share the ZIP with complete integration guide above. 🚀
