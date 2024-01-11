--liquibase formatted sql
--changeset Minich:1
CREATE TABLE IF NOT EXISTS houses_persons (
    houses_id BIGINT NOT NULL REFERENCES houses(id),
    persons_id BIGINT NOT NULL REFERENCES persons(id),
    PRIMARY KEY (houses_id, persons_id)
);
