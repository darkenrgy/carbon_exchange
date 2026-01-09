# Carbon Traders Blockchain Smart Contracts

Complete blockchain implementation for carbon credit tokenization and retirement.

## 📋 Overview

- **Smart Contract:** ERC1155 multi-token standard for carbon credits
- **Framework:** Hardhat + Solidity 0.8.20
- **Network:** Local (Hardhat), Testnet (Sepolia), or Mainnet
- **Integration:** Web3j (Java), ethers.js (React)

## 🚀 Quick Start

### 1. Install & Test
```bash
npm install
npm run auto:local
```

### 2. Local Development
```bash
# Terminal 1: Start blockchain node
npx hardhat node

# Terminal 2: Deploy contract
npm run deploy:local

# Terminal 3: Backend/Frontend connect to localhost:8545
```

### 3. Testnet Deployment
```bash
cp .env.example .env
# Edit .env: RPC_URL, PRIVATE_KEY, ADMIN_ADDRESS
npm run deploy:sepolia
```

## 📁 Project Structure

```
blockchain/
├── contracts/
│   └── CarbonCreditToken.sol          # Main smart contract
├── scripts/
│   ├── auto_local.js                  # Automated demo
│   ├── issue_batch.js                 # Issue credits (CLI)
│   └── retire_batch.js                # Retire credits (CLI)
├── test/
│   └── carbon_token.test.js           # Test suite
├── java-integration/
│   ├── BlockchainService.java         # Java service layer
│   ├── BlockchainController.java      # REST endpoints
│   ├── pom-dependencies.xml           # Maven dependencies
│   └── application-yml-config.txt     # Spring config
├── deploy/
│   └── 01_deploy_carbon_token.js      # Deployment script
├── QUICK_START.md                     # ⭐ Start here
├── SETUP_AND_INTEGRATION.md           # Full integration guide
├── .env.example                       # Environment template
├── hardhat.config.js                  # Hardhat configuration
└── package.json                       # Node dependencies
```

## 🔑 Key Features

### Smart Contract (`CarbonCreditToken.sol`)

**Issuance:**
- Role-based access control (ISSUER_ROLE)
- Tracks comprehensive metadata per batch:
  - Farmer registration number
  - Project location
  - Vintage, verification, issuance, tokenization dates
  - Verification data hash (IPFS)

**Retirement:**
- Burn mechanism with tracking
- Records eco-action reason
- Maintains full history

**Metadata:**
- ERC1155 multi-token standard
- Sequential token IDs
- URI resolution for JSON metadata

### Java Integration

**BlockchainService:**
- Web3j connection management
- Issue, retire, query operations
- Error handling and logging

**BlockchainController:**
- REST endpoints for frontend
- Health check, balance query
- Full audit trail

### Testing

```bash
# Run all tests
npm test

# Compile contract
npm run compile

# Automated demo (no external RPC needed)
npm run auto:local
```

## 📊 Smart Contract Interface

### Issue Batch
```solidity
function issueBatch(
    address to,
    uint256 amount,
    string farmerRegistrationNo,
    string location,
    uint256 vintageDate,
    uint256 verificationDate,
    uint256 issuanceDate,
    uint256 tokenizationDate,
    string verificationDataHash
) → uint256 tokenId
```

### Retire Credits
```solidity
function retire(
    uint256 tokenId,
    uint256 amount,
    string reason
)
```

### Query Metadata
```solidity
function getBatch(uint256 tokenId) → Batch
function getRetirementHistory(uint256 tokenId) → RetirementRecord[]
```

## 🔗 Integration Points

### Java Backend
1. Copy `java-integration/BlockchainService.java` → your backend
2. Copy `java-integration/BlockchainController.java` → your backend
3. Add Web3j dependencies (see `pom-dependencies.xml`)
4. Configure `application.yml` with blockchain settings
5. Wire service into existing carbon credit flows

### React Frontend
1. Install: `npm install ethers react-toastify`
2. Create Web3 context for wallet connection
3. Use axios to call `/api/v1/blockchain/` endpoints
4. Display batch metadata and retirement history

### MySQL Database
Run migrations:
```sql
ALTER TABLE carbon_credit ADD blockchain_token_id BIGINT;
ALTER TABLE carbon_credit ADD blockchain_tx_hash VARCHAR(255);

CREATE TABLE carbon_retirement (
    id BIGINT PRIMARY KEY,
    token_id BIGINT,
    buyer_id BIGINT,
    amount BIGINT,
    eco_action VARCHAR(500),
    blockchain_tx_hash VARCHAR(255),
    retirement_date TIMESTAMP
);
```

## 📖 Documentation

- **[QUICK_START.md](./QUICK_START.md)** - 5-minute setup guide
- **[SETUP_AND_INTEGRATION.md](./SETUP_AND_INTEGRATION.md)** - Complete integration with all three systems
- **[contracts/CarbonCreditToken.sol](./contracts/CarbonCreditToken.sol)** - Detailed contract comments
- **[test/carbon_token.test.js](./test/carbon_token.test.js)** - Usage examples

## 🛠️ Commands

```bash
# Setup
npm install                    # Install dependencies
npm run compile               # Compile smart contract

# Testing
npm test                      # Run test suite
npm run auto:local            # Run automated demo

# Local Development
npx hardhat node              # Start local blockchain
npm run deploy:local          # Deploy to localhost

# Testnet
npm run deploy:sepolia        # Deploy to Sepolia testnet
npm run issue                 # Issue batch (uses .env)
npm run retire                # Retire credits (uses .env)
```

## 🔐 Security

### Private Key Management
- Never commit `.env` file
- Use environment variables in production
- Rotate keys regularly
- Use hardware wallet for mainnet

### Contract Auditing
- Contract uses OpenZeppelin audited libraries
- Role-based access control
- Over-retirement protection
- Event logging for all actions

### Gas Optimization
- viaIR enabled in Hardhat config
- Optimized for 200 runs
- ERC1155 efficient for batch operations

## 📞 Support & Troubleshooting

**Cannot connect to blockchain?**
- Check RPC URL is accessible
- Verify private key has funds (testnet: use faucet)

**Contract deployment fails?**
- Ensure .env is properly configured
- Check gas limit settings in hardhat.config.js
- Verify account has enough ETH for gas

**Java compilation errors?**
- Ensure Web3j dependencies installed
- Check Java version (11+ recommended)
- Generate contract bindings from ABI

See [SETUP_AND_INTEGRATION.md](./SETUP_AND_INTEGRATION.md) for detailed troubleshooting.

## 📄 License

MIT License - See project root

---

**Ready to integrate? Read [QUICK_START.md](./QUICK_START.md) first! 🚀**
