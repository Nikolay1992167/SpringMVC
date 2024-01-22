--liquibase formatted sql
--changeset Minich:4
CREATE TYPE type AS ENUM ('OWNER', 'TENANT');

CREATE TABLE house_history (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    house_id BIGINT NOT NULL REFERENCES houses(id),
    person_id BIGINT NOT NULL REFERENCES persons(id),
    date TIMESTAMP NOT NULL,
    type_person type NOT NULL
);