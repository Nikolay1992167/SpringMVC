--liquibase formatted sql
--changeset Minich:4
CREATE TYPE "TypePerson" AS ENUM('OWNER', 'TENANT');

CREATE TABLE house_history (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    house_id BIGINT NOT NULL,
    person_id BIGINT NOT NULL,
    date TIMESTAMP NOT NULL,
    person_type "TypePerson" NOT NULL
);


