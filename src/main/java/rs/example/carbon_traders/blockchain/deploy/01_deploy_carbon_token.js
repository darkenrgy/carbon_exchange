const hre = require("hardhat");
require("dotenv").config();

async function main() {
  const [deployer] = await hre.ethers.getSigners();
  const baseURI = process.env.BASE_URI || "https://example.com/metadata/";
  const admin = process.env.ADMIN_ADDRESS && process.env.ADMIN_ADDRESS !== ""
    ? process.env.ADMIN_ADDRESS
    : deployer.address;

  console.log("Deploying CarbonCreditToken...");
  console.log("Deployer:", deployer.address);
  console.log("Admin:   ", admin);
  console.log("Base URI:", baseURI);

  const Factory = await hre.ethers.getContractFactory("CarbonCreditToken");
  const contract = await Factory.deploy(baseURI, admin);
  await contract.waitForDeployment();

  const address = await contract.getAddress();
  console.log("CarbonCreditToken deployed at:", address);
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
