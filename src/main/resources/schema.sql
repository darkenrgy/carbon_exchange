CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(30) NOT NULL,
                       status VARCHAR(30) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE farmers (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         user_id BIGINT NOT NULL,
                         name VARCHAR(100),
                         land_area DOUBLE,
                         crop_type VARCHAR(50),
                         verification_status VARCHAR(30),
                         FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE companies (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           user_id BIGINT NOT NULL,
                           company_name VARCHAR(150),
                           total_credits DOUBLE,
                           FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE carbon_credits (
                                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                farmer_id BIGINT,
                                amount DOUBLE,
                                status VARCHAR(30),
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (farmer_id) REFERENCES farmers(id)
);
