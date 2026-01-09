const hre = require("hardhat");
require("dotenv").config();

async function main() {
  const {
    CONTRACT_ADDRESS,
    RECIPIENT,
    ISSUE_AMOUNT,
    PROJECT_ID,
    DATA_HASH
  } = process.env;

  if (!CONTRACT_ADDRESS) throw new Error("CONTRACT_ADDRESS is required");
  if (!RECIPIENT) throw new Error("RECIPIENT is required");

  const amount = ISSUE_AMOUNT ? BigInt(ISSUE_AMOUNT) : 0n;
  if (amount <= 0) throw new Error("ISSUE_AMOUNT must be > 0");

  const projectId = PROJECT_ID || "project";
  const dataHash = DATA_HASH || "ipfs://cid";

  const [signer] = await hre.ethers.getSigners();
  console.log("Issuer:", signer.address);

  const contract = await hre.ethers.getContractAt("CarbonCreditToken", CONTRACT_ADDRESS);

  // Predict next token id via static call
  const predictedId = await contract.issueBatch.staticCall(
    RECIPIENT,
    amount,
    projectId,
    dataHash
  );

  console.log("Issuing tokenId", predictedId.toString());
  const tx = await contract.issueBatch(RECIPIENT, amount, projectId, dataHash);
  const receipt = await tx.wait();

  console.log("Transaction hash:", receipt.hash);
  console.log("Gas used:", receipt.gasUsed?.toString());
  console.log("BatchIssued event args:", receipt.logs?.map((log) => log.args).filter(Boolean));
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
