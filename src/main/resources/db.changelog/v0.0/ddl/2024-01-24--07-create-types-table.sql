--liquibase formatted sql
--changeset Minich:7
CREATE TABLE IF NOT EXISTS types (
	id INTEGER PRIMARY KEY NOT NULL,
	name varchar(7) UNIQUE NOT NULL
);