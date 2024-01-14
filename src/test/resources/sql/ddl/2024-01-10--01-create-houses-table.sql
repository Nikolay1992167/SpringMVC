--liquibase formatted sql
--changeset Minich:1
CREATE TABLE IF NOT EXISTS  houses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    uuid UUID NOT NULL UNIQUE,
    area VARCHAR(50) NOT NULL,
    country VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    number BIGINT NOT NULL,
    create_date TIMESTAMP NOT NULL,
    UNIQUE (area, country, city, street, number, create_date)
);