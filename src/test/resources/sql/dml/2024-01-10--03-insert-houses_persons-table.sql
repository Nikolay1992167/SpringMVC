--liquibase formatted sql
--changeset Minich:6
INSERT INTO houses_persons(houses_id, persons_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (2, 4),
       (3, 5),
       (4, 6),
       (4, 7),
       (5, 8),
       (5, 9),
       (5, 10);