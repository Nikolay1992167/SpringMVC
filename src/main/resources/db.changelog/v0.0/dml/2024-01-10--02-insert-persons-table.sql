--liquibase formatted sql
--changeset Minich:2
INSERT INTO persons(uuid, name, surname, sex, passport_series, passport_number, create_date, update_date, house_id)
VALUES ('9aa78d35-fb66-45a6-8570-f81513ef8272', 'Марина', 'Громкая', 'FEMALE', 'HB', '123456', '2022-03-06T10:00:00.000', '2022-03-06T10:00:00.000', 1),
       ('922e0213-e543-48ef-b8cb-92592afd5100', 'Иван', 'Рогозин', 'MALE', 'HM', '234567', '2022-01-07T16:00:00.000', '2022-01-07T16:00:00.000', 1),
       ('00cc3880-2e7a-47a8-b688-91e9565e972d', 'Женя', 'Древко', 'MALE', 'WE', '345678', '2022-09-08T17:00:00.000', '2022-09-08T17:00:00.000', 1),
       ('24277f25-81ee-4925-885c-a639d0211dde', 'Вася', 'Гусич', 'MALE', 'GH', '456789', '2021-01-09T18:00:00.000', '2021-01-09T18:00:00.000', 3),
       ('e1b6fd29-49cd-499d-ac9b-0dee226aed14', 'Инна', 'Цибулько', 'FEMALE', 'HO', '567890', '2023-10-10T19:00:00.000', '2023-10-10T19:00:00.000', 3),
       ('863db796-cf16-4c67-ad24-710d0d2f0341', 'Петя', 'Свист', 'MALE', 'HL', '678901', '2023-05-11T11:00:00.000', '2023-05-11T11:00:00.000', 3),
       ('f188d7be-146b-4668-a729-09a2d4fdc784', 'Валя', 'Калина', 'FEMALE', 'MN', '789012', '2021-01-12T21:00:00.000', '2021-01-12T21:00:00.000', 4),
       ('3df38f0a-09bb-4bbc-a80c-2f827b6f9d75', 'Игорь', 'Гнедов', 'MALE', 'HP', '890123', '2022-04-13T22:00:00.000', '2022-04-13T22:00:00.000', 4),
       ('63a1faca-a963-4d4b-bfb9-2dafaedc36fe', 'Леня', 'Дудич', 'MALE', 'HR', '901234', '2021-01-14T13:00:00.000', '2021-01-14T13:00:00.000', 5),
       ('40291d40-5948-448c-b66a-d09591d3500f', 'Олег', 'Луцко', 'MALE', 'HB', '012345', '2021-09-18T15:00:00.000', '2021-09-18T15:00:00.000', 5);