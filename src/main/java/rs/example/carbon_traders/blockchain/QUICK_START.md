# Quick Start for Blockchain Integration

## For Your Friend - Getting Started in 5 Minutes

### 1️⃣ Extract & Install (2 min)
```bash
unzip blockchain-carbon-traders.zip
cd blockchain
npm install
```

### 2️⃣ Test It Works Locally (1 min)
```bash
npm run auto:local
```

You should see:
```
✅ Compiled successfully
✅ Deployed contract
✅ Issued 100 carbon credits
✅ Retired 25 credits
✅ Batch metadata displayed
```

---

## 🔗 Connect to Your Full System

### Phase 1: Local Development (Testing)
No blockchain costs, everything on your computer.

```
Browser (React)
    ↓ (HTTP)
Java Backend (localhost:8080)
    ↓ (Web3j)
Local Hardhat Node (localhost:8545)
    ↓
Smart Contract
```

**Setup:**
```bash
# Terminal 1: Start local blockchain node
cd blockchain
npx hardhat node

# Terminal 2: Deploy contract
npm run deploy:local

# Terminal 3: Start Java backend
cd ../
mvn spring-boot:run

# Terminal 4: Start React frontend
cd frontend
npm start
```

---

### Phase 2: Testnet (Before Production)
Use Sepolia testnet (free test ETH available).

**Get Test ETH:** https://sepolia-faucet.pk910.de/

**Setup:**
```bash
cd blockchain

# Copy environment
cp .env.example .env

# Edit .env:
RPC_URL="https://sepolia.infura.io/v3/YOUR_API_KEY"  # Get free at infura.io
PRIVATE_KEY="0x..."  # Your testnet wallet private key
ADMIN_ADDRESS="0x..."  # Your wallet address

# Deploy to Sepolia
npm run deploy:sepolia

# Copy deployed address to Java config
```

**Update Java:**
```yaml
# application.yml
blockchain:
  rpc-url: "https://sepolia.infura.io/v3/YOUR_API_KEY"
  contract-address: "0x..."  # From deploy output
  private-key: "0x..."
  admin-address: "0x..."
```

---

### Phase 3: Production (Real Money)
Mainnet deployment—use after thorough testing.

**Same steps as Testnet, but:**
- Use Mainnet RPC URL
- Use real ETH (costs gas)
- Deploy once, reuse forever
- Update Java config

---

## 📝 Integration Checklist

- [ ] Extract blockchain.zip
- [ ] Run `npm install` in blockchain folder
- [ ] Test with `npm run auto:local`
- [ ] Read full SETUP_AND_INTEGRATION.md
- [ ] Copy Java BlockchainService.java to backend
- [ ] Add Web3j dependency to pom.xml
- [ ] Create blockchain config in application.yml
- [ ] Copy React components to frontend
- [ ] Install ethers.js in frontend
- [ ] Add MySQL tables from schema
- [ ] Wire Java service calls in existing code
- [ ] Test end-to-end flow

---

## 🔑 Key Files

| File | Purpose |
|------|---------|
| `contracts/CarbonCreditToken.sol` | Smart contract |
| `scripts/auto_local.js` | Demo automation |
| `test/carbon_token.test.js` | Test suite |
| `SETUP_AND_INTEGRATION.md` | **Full integration guide** |
| `.env.example` | Environment template |
| `hardhat.config.js` | Blockchain configuration |

---

## 🆘 Troubleshooting

**"Cannot find module" after npm install?**
```bash
rm -rf node_modules package-lock.json
npm install
```

**Port 8545 already in use?**
```bash
# Change in hardhat.config.js or use different port
npx hardhat node --port 8546
```

**Contract deployment fails?**
- Check RPC URL is correct
- Ensure private key has funds (testnet faucet)
- Check .env file is not committed (security risk)

**Java can't connect to blockchain?**
- Verify RPC_URL in application.yml
- Test with: `curl https://your-rpc-url`

---

## 💬 Support

If questions:
1. Check SETUP_AND_INTEGRATION.md (detailed guide)
2. Review comments in Java/JS code
3. Test locally first with `npm run auto:local`
4. Check contract events in block explorer (Etherscan)

---

**Ready to go! 🚀**
