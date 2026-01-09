const { expect } = require("chai");
const { ethers } = require("hardhat");

describe("CarbonCreditToken", function () {
  const baseURI = "https://example.com/metadata/";
  let token;
  let deployer;
  let issuer;
  let farmer;
  let buyer;

  beforeEach(async () => {
    [deployer, issuer, farmer, buyer] = await ethers.getSigners();
    const Factory = await ethers.getContractFactory("CarbonCreditToken");
    token = await Factory.deploy(baseURI, deployer.address);
    await token.waitForDeployment();

    // Grant issuer role to an additional account
    await token.grantRole(await token.ISSUER_ROLE(), issuer.address);
  });

  it("assigns admin and issuer roles on deploy", async () => {
    const adminRole = await token.DEFAULT_ADMIN_ROLE();
    const issuerRole = await token.ISSUER_ROLE();

    expect(await token.hasRole(adminRole, deployer.address)).to.equal(true);
    expect(await token.hasRole(issuerRole, deployer.address)).to.equal(true);
    expect(await token.hasRole(issuerRole, issuer.address)).to.equal(true);
  });

  it("issues a batch with comprehensive metadata", async () => {
    const amount = 100n;
    const farmerRegistrationNo = "FARMER-001";
    const location = "45.4215° N, 75.6972° W";
    const now = Math.floor(Date.now() / 1000);
    const vintageDate = now - 30 * 24 * 60 * 60;
    const verificationDate = now - 15 * 24 * 60 * 60;
    const issuanceDate = now - 5 * 24 * 60 * 60;
    const tokenizationDate = now;
    const verificationDataHash = "ipfs://Qm...";

    const predictedId = await token.connect(issuer).issueBatch.staticCall(
      farmer.address,
      amount,
      farmerRegistrationNo,
      location,
      vintageDate,
      verificationDate,
      issuanceDate,
      tokenizationDate,
      verificationDataHash
    );

    const tx = await token.connect(issuer).issueBatch(
      farmer.address,
      amount,
      farmerRegistrationNo,
      location,
      vintageDate,
      verificationDate,
      issuanceDate,
      tokenizationDate,
      verificationDataHash
    );
    await tx.wait();

    const batch = await token.getBatch(predictedId);
    expect(batch.farmerRegistrationNo).to.equal(farmerRegistrationNo);
    expect(batch.location).to.equal(location);
    expect(batch.vintageDate).to.equal(vintageDate);
    expect(batch.verificationDate).to.equal(verificationDate);
    expect(batch.issuanceDate).to.equal(issuanceDate);
    expect(batch.tokenizationDate).to.equal(tokenizationDate);
    expect(batch.verificationDataHash).to.equal(verificationDataHash);
    expect(batch.issued).to.equal(amount);
    expect(batch.retired).to.equal(0n);
  });

  it("blocks non-issuers from minting", async () => {
    const issuerRole = await token.ISSUER_ROLE();
    await expect(
      token.connect(buyer).issueBatch(buyer.address, 10n, "p", "d")
    )
      .to.be.revertedWithCustomError(token, "AccessControlUnauthorizedAccount")
      .withArgs(buyer.address, issuerRole);
  });

  it("retires credits, records reason, and tracks history", async () => {
    const amount = 50n;
    const retireAmount = 20n;
    const reason = "Reforestation North America";

    const now = Math.floor(Date.now() / 1000);
    const tokenId = await token.issueBatch.staticCall(
      farmer.address,
      amount,
      "FARMER-002",
      "Location",
      now - 30 * 24 * 60 * 60,
      now - 15 * 24 * 60 * 60,
      now - 5 * 24 * 60 * 60,
      now,
      "ipfs://..."
    );
    const tx = await token.issueBatch(
      farmer.address,
      amount,
      "FARMER-002",
      "Location",
      now - 30 * 24 * 60 * 60,
      now - 15 * 24 * 60 * 60,
      now - 5 * 24 * 60 * 60,
      now,
      "ipfs://..."
    );
    await tx.wait();

    await token.connect(farmer).retire(tokenId, retireAmount, reason);

    expect(await token.balanceOf(farmer.address, tokenId)).to.equal(amount - retireAmount);
    const batch = await token.getBatch(tokenId);
    expect(batch.retired).to.equal(retireAmount);

    const history = await token.getRetirementHistory(tokenId);
    expect(history.length).to.equal(1);
    expect(history[0].amount).to.equal(retireAmount);
    expect(history[0].reason).to.equal(reason);
    expect(history[0].retiredBy).to.equal(farmer.address);
  });

  it("tops up an existing batch and refreshes metadata", async () => {
    const initial = 30n;
    const topUp = 20n;

    const now = Math.floor(Date.now() / 1000);
    const tokenId = await token.issueBatch.staticCall(
      farmer.address,
      initial,
      "FARMER-003",
      "Location",
      now - 30 * 24 * 60 * 60,
      now - 15 * 24 * 60 * 60,
      now - 5 * 24 * 60 * 60,
      now,
      "ipfs://old"
    );
    await token.issueBatch(
      farmer.address,
      initial,
      "FARMER-003",
      "Location",
      now - 30 * 24 * 60 * 60,
      now - 15 * 24 * 60 * 60,
      now - 5 * 24 * 60 * 60,
      now,
      "ipfs://old"
    );

    await token.topUp(farmer.address, tokenId, topUp, "ipfs://new");

    const batch = await token.getBatch(tokenId);
    expect(batch.issued).to.equal(initial + topUp);
    expect(batch.verificationDataHash).to.equal("ipfs://new");
    expect(await token.balanceOf(farmer.address, tokenId)).to.equal(initial + topUp);
  });
});
