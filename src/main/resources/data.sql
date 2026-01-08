INSERT INTO users (email, password, role, status)
VALUES
    ('admin@carbon.com', '$2a$10$encodedpassword', 'ADMIN', 'ACTIVE'),
    ('farmer@carbon.com', '$2a$10$encodedpassword', 'FARMER', 'ACTIVE'),
    ('company@carbon.com', '$2a$10$encodedpassword', 'COMPANY', 'ACTIVE');

INSERT INTO farmers (user_id, name, land_area, crop_type, verification_status)
VALUES
    (2, 'Ramesh Kumar', 5.5, 'Wheat', 'PENDING');

INSERT INTO companies (user_id, company_name, total_credits)
VALUES
    (3, 'Green Energy Pvt Ltd', 0);
