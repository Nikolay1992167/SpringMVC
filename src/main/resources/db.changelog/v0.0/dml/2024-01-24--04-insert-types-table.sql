--liquibase formatted sql
--changeset Minich:11
INSERT INTO types(id, name)
VALUES (1, 'OWNER'),
       (2, 'TENANT');