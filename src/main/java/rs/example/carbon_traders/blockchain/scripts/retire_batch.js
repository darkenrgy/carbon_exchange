const hre = require("hardhat");
require("dotenv").config();

async function main() {
  const { CONTRACT_ADDRESS, TOKEN_ID, RETIRE_AMOUNT } = process.env;

  if (!CONTRACT_ADDRESS) throw new Error("CONTRACT_ADDRESS is required");
  const tokenId = TOKEN_ID ? BigInt(TOKEN_ID) : 0n;
  if (tokenId <= 0n) throw new Error("TOKEN_ID must be > 0");

  const amount = RETIRE_AMOUNT ? BigInt(RETIRE_AMOUNT) : 0n;
  if (amount <= 0n) throw new Error("RETIRE_AMOUNT must be > 0");

  const [caller] = await hre.ethers.getSigners();
  console.log("Caller:", caller.address);

  const contract = await hre.ethers.getContractAt("CarbonCreditToken", CONTRACT_ADDRESS);

  console.log(`Retiring ${amount} of token ${tokenId}`);
  const tx = await contract.retire(tokenId, amount);
  const receipt = await tx.wait();

  console.log("Transaction hash:", receipt.hash);
  console.log("Gas used:", receipt.gasUsed?.toString());
  console.log("Retired event args:", receipt.logs?.map((log) => log.args).filter(Boolean));
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
