create table customers (
    customer_id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    contact VARCHAR(255),
    country VARCHAR(255),
    dob VARCHAR(255),
    email VARCHAR(255),
    first_name VARCHAR(255),
    gender VARCHAR(255),
    last_name VARCHAR(255)
) engine=InnoDB
