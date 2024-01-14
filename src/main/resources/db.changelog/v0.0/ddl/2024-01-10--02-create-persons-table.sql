--liquibase formatted sql
--changeset Minich:2
CREATE TABLE IF NOT EXISTS persons (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    uuid UUID NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    sex VARCHAR(10),
    passport_series VARCHAR(50) NOT NULL,
    passport_number VARCHAR(50) NOT NULL,
    create_date TIMESTAMP NOT NULL,
    update_date TIMESTAMP NOT NULL,
    house_id BIGINT REFERENCES houses(id),
    UNIQUE (passport_series, passport_number)
);