const hre = require("hardhat");
require("dotenv").config();

async function main() {
  const baseURI = process.env.BASE_URI || "https://example.com/metadata/";
  const [admin, issuer, farmer] = await hre.ethers.getSigners();

  console.log("Running automated local flow on in-process hardhat network...\n");
  console.log("Admin:  ", admin.address);
  console.log("Issuer: ", issuer.address);
  console.log("Farmer: ", farmer.address);

  // Deploy
  const Factory = await hre.ethers.getContractFactory("CarbonCreditToken");
  const token = await Factory.deploy(baseURI, admin.address);
  await token.waitForDeployment();
  const address = await token.getAddress();
  console.log("Deployed CarbonCreditToken at", address);

  // Grant issuer role
  await token.grantRole(await token.ISSUER_ROLE(), issuer.address);
  console.log("Granted ISSUER_ROLE to", issuer.address);

  // Issue a batch to farmer with full metadata
  const issueAmount = 100n;
  const farmerRegistrationNo = "FARMER-12345";
  const location = "45.4215° N, 75.6972° W"; // Example coords
  const now = Math.floor(Date.now() / 1000);
  const vintageDate = now - 30 * 24 * 60 * 60;      // 30 days ago
  const verificationDate = now - 15 * 24 * 60 * 60; // 15 days ago
  const issuanceDate = now - 5 * 24 * 60 * 60;      // 5 days ago
  const tokenizationDate = now;
  const verificationDataHash = "ipfs://QmABCDEF...";

  const tokenId = await token
    .connect(issuer)
    .issueBatch.staticCall(
      farmer.address,
      issueAmount,
      farmerRegistrationNo,
      location,
      vintageDate,
      verificationDate,
      issuanceDate,
      tokenizationDate,
      verificationDataHash
    );

  const issueTx = await token
    .connect(issuer)
    .issueBatch(
      farmer.address,
      issueAmount,
      farmerRegistrationNo,
      location,
      vintageDate,
      verificationDate,
      issuanceDate,
      tokenizationDate,
      verificationDataHash
    );
  await issueTx.wait();
  console.log(`Issued tokenId ${tokenId} x ${issueAmount} to farmer (${farmerRegistrationNo})`);

  // Retire a portion with eco action reason
  const retireAmount = 25n;
  const reason = "Reforestation project in North America";
  const retireTx = await token.connect(farmer).retire(tokenId, retireAmount, reason);
  await retireTx.wait();
  console.log(`Retired ${retireAmount} from tokenId ${tokenId} (${reason})`);

  // Show balances and metadata
  const balance = await token.balanceOf(farmer.address, tokenId);
  const batch = await token.getBatch(tokenId);
  const history = await token.getRetirementHistory(tokenId);
  
  console.log("\n=== BATCH METADATA ===");
  console.log("Farmer Reg No:", batch.farmerRegistrationNo);
  console.log("Location:", batch.location);
  console.log("Vintage Date:", new Date(Number(batch.vintageDate) * 1000).toISOString());
  console.log("Verification Date:", new Date(Number(batch.verificationDate) * 1000).toISOString());
  console.log("Issuance Date:", new Date(Number(batch.issuanceDate) * 1000).toISOString());
  console.log("Tokenization Date:", new Date(Number(batch.tokenizationDate) * 1000).toISOString());
  console.log("Verification Hash:", batch.verificationDataHash);
  
  console.log("\n=== SUPPLY STATUS ===");
  console.log("Farmer balance:", balance.toString());
  console.log("Total issued:", batch.issued.toString());
  console.log("Total retired:", batch.retired.toString());
  
  console.log("\n=== RETIREMENT HISTORY ===");
  history.forEach((record, i) => {
    console.log(`Retirement ${i + 1}:`);
    console.log("  Retired by:", record.retiredBy);
    console.log("  Amount:", record.amount.toString());
    console.log("  Date:", new Date(Number(record.retirementDate) * 1000).toISOString());
    console.log("  Reason:", record.reason);
  });

  console.log("\nAutomation complete.");
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
