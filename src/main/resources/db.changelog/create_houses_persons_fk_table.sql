CREATE TABLE IF NOT EXISTS houses_persons (
    houses_id BIGINT NOT NULL,
    persons_id BIGINT NOT NULL,
    PRIMARY KEY (houses_id, persons_id)
);

ALTER TABLE houses_persons
ADD CONSTRAINT fk_houses_persons_houses FOREIGN KEY (houses_id) REFERENCES houses(id);
ALTER TABLE houses_persons
ADD CONSTRAINT fk_houses_persons_persons FOREIGN KEY (persons_id) REFERENCES persons(id);