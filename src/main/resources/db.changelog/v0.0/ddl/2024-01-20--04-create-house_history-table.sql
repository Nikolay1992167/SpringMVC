--liquibase formatted sql
--changeset Minich:4
CREATE TABLE house_history (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    house_id BIGINT NOT NULL,
    person_id BIGINT NOT NULL,
    date TIMESTAMP NOT NULL,
    type_id INTEGER NOT NULL REFERENCES types
);


