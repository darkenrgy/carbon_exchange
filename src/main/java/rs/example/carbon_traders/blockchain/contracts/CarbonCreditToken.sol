// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import "@openzeppelin/contracts/token/ERC1155/ERC1155.sol";
import "@openzeppelin/contracts/access/AccessControl.sol";
import "@openzeppelin/contracts/utils/Strings.sol";

/**
 * @title CarbonCreditToken
 * @notice ERC1155 carbon credits with comprehensive metadata tracking.
 *         Tracks farmer registration, vintage/issuance/verification/tokenization dates,
 *         location, and retirement records with timestamps.
 */
contract CarbonCreditToken is ERC1155, AccessControl {
    using Strings for uint256;

    bytes32 public constant ISSUER_ROLE = keccak256("ISSUER_ROLE");
    bytes32 public constant VERIFIER_ROLE = keccak256("VERIFIER_ROLE");

    struct Batch {
        // Farmer & Project Info
        string farmerRegistrationNo;    // Unique farmer ID from registration
        string location;                // GPS or administrative location

        // Date Tracking (unix timestamps)
        uint256 vintageDate;            // When credits were earned/generated
        uint256 verificationDate;       // When verified as authentic
        uint256 issuanceDate;           // When issued to blockchain
        uint256 tokenizationDate;       // When converted to tokens

        // Supply Tracking
        uint256 issued;                 // Total minted
        uint256 retired;                // Total burned

        // Data Integrity
        string verificationDataHash;    // IPFS CID of verification evidence
    }

    struct RetirementRecord {
        address retiredBy;
        uint256 retirementDate;         // Timestamp when retired
        uint256 amount;
        string reason;                  // Optional: eco action description
    }

    // tokenId => Batch metadata
    mapping(uint256 => Batch) public batches;
    // tokenId => array of retirement records
    mapping(uint256 => RetirementRecord[]) public retirementRecords;
    // Simple incremental ID counter
    uint256 private _nextTokenId = 1;

    event BatchIssued(
        uint256 indexed tokenId,
        string farmerRegistrationNo,
        string location,
        uint256 vintageDate,
        uint256 verificationDate,
        uint256 issuanceDate,
        uint256 tokenizationDate,
        uint256 amount,
        address indexed issuer
    );

    event Retired(
        uint256 indexed tokenId,
        address indexed account,
        uint256 amount,
        uint256 retirementDate,
        string reason
    );

    constructor(string memory baseURI, address admin) ERC1155(baseURI) {
        _grantRole(DEFAULT_ADMIN_ROLE, admin);
        _grantRole(ISSUER_ROLE, admin);
        _grantRole(VERIFIER_ROLE, admin);
    }

    /**
     * @notice Issue a new carbon credit batch with comprehensive metadata.
     * @param to Recipient address (farmer wallet)
     * @param amount Number of credits (tons of CO2e)
     * @param farmerRegistrationNo Unique farmer registration ID
     * @param location Geographic location of carbon project
     * @param vintageDate Unix timestamp when credits were earned
     * @param verificationDate Unix timestamp when verified
     * @param issuanceDate Unix timestamp when issued
     * @param tokenizationDate Unix timestamp when tokenized
     * @param verificationDataHash IPFS CID of verification package
     * @return tokenId Newly created batch token ID
     */
    function issueBatch(
        address to,
        uint256 amount,
        string memory farmerRegistrationNo,
        string memory location,
        uint256 vintageDate,
        uint256 verificationDate,
        uint256 issuanceDate,
        uint256 tokenizationDate,
        string memory verificationDataHash
    ) external onlyRole(ISSUER_ROLE) returns (uint256 tokenId) {
        require(amount > 0, "amount zero");
        require(to != address(0), "zero recipient");
        require(vintageDate > 0 && verificationDate > 0, "invalid dates");

        tokenId = _nextTokenId++;
        batches[tokenId] = Batch({
            farmerRegistrationNo: farmerRegistrationNo,
            location: location,
            vintageDate: vintageDate,
            verificationDate: verificationDate,
            issuanceDate: issuanceDate,
            tokenizationDate: tokenizationDate,
            issued: amount,
            retired: 0,
            verificationDataHash: verificationDataHash
        });

        _mint(to, tokenId, amount, "");
        emit BatchIssued(
            tokenId,
            farmerRegistrationNo,
            location,
            vintageDate,
            verificationDate,
            issuanceDate,
            tokenizationDate,
            amount,
            msg.sender
        );
    }

    /**
     * @notice Retire (burn) credits and record the eco action.
     * @param tokenId Batch ID to retire
     * @param amount Amount to retire
     * @param reason Description of eco action performed
     */
    function retire(
        uint256 tokenId,
        uint256 amount,
        string memory reason
    ) external {
        require(amount > 0, "amount zero");
        _burn(msg.sender, tokenId, amount);

        Batch storage b = batches[tokenId];
        b.retired += amount;
        require(b.retired <= b.issued, "over-retire");

        uint256 retirementDate = block.timestamp;
        retirementRecords[tokenId].push(
            RetirementRecord({
                retiredBy: msg.sender,
                retirementDate: retirementDate,
                amount: amount,
                reason: reason
            })
        );

        emit Retired(tokenId, msg.sender, amount, retirementDate, reason);
    }

    /**
     * @notice Add supply to an existing batch (e.g., corrected verification).
     * @param to Recipient of additional credits
     * @param tokenId Batch ID
     * @param amount Amount to add
     * @param newVerificationDataHash Updated IPFS CID if verification changes
     */
    function topUp(
        address to,
        uint256 tokenId,
        uint256 amount,
        string memory newVerificationDataHash
    ) external onlyRole(ISSUER_ROLE) {
        require(amount > 0, "amount zero");
        Batch storage b = batches[tokenId];
        require(b.issued > 0, "batch missing");

        b.issued += amount;
        if (bytes(newVerificationDataHash).length > 0) {
            b.verificationDataHash = newVerificationDataHash;
        }

        _mint(to, tokenId, amount, "");
    }

    /**
     * @notice Get all retirement records for a token.
     */
    function getRetirementHistory(uint256 tokenId)
        external
        view
        returns (RetirementRecord[] memory)
    {
        return retirementRecords[tokenId];
    }

    /**
     * @notice Get batch metadata.
     */
    function getBatch(uint256 tokenId)
        external
        view
        returns (Batch memory)
    {
        return batches[tokenId];
    }

    /**
     * @notice Override URI to expose per-token metadata (expects `{id}` replacement in base URI).
     */
    function uri(uint256 tokenId) public view override returns (string memory) {
        return string(abi.encodePacked(super.uri(tokenId), tokenId.toString(), ".json"));
    }

    /**
     * @dev Required override for AccessControl + ERC1155 inheritance.
     */
    function supportsInterface(bytes4 interfaceId)
        public
        view
        override(ERC1155, AccessControl)
        returns (bool)
    {
        return super.supportsInterface(interfaceId);
    }
}
