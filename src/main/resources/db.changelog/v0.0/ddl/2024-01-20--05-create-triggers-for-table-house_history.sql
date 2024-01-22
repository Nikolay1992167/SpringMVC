--liquibase formatted sql
--changeset Minich:5
CREATE OR REPLACE FUNCTION person_create_house_func() RETURNS TRIGGER
LANGUAGE plpgsql AS
$$
BEGIN
  INSERT INTO house_history (house_id, person_id, date, type_person)
  VALUES (NEW.house_id, NEW.id, CURRENT_DATE, 'TENANT');
  RETURN NEW;
END;
$$;

CREATE TRIGGER person_create_house
AFTER INSERT ON persons
FOR EACH ROW
EXECUTE FUNCTION person_create_house_func();

CREATE OR REPLACE FUNCTION person_change_house_func() RETURNS TRIGGER
LANGUAGE plpgsql AS
$$
BEGIN
  INSERT INTO house_history (house_id, person_id, date, type_person)
  VALUES (NEW.house_id, NEW.id, CURRENT_DATE, 'TENANT');
  RETURN NEW;
END;
$$;

CREATE TRIGGER person_change_house
AFTER UPDATE OF house_id ON persons
FOR EACH ROW
EXECUTE FUNCTION person_change_house_func();

CREATE OR REPLACE FUNCTION house_owner_change_func() RETURNS TRIGGER
LANGUAGE plpgsql AS
$$
BEGIN
  IF TG_OP = 'INSERT' THEN
    INSERT INTO house_history (house_id, person_id, date, type_person)
    VALUES (NEW.houses_id, NEW.persons_id, CURRENT_DATE, 'OWNER');
    RETURN NEW;
  ELSIF TG_OP = 'DELETE' THEN
    INSERT INTO house_history (house_id, person_id, date, type_person)
    VALUES (OLD.houses_id, OLD.persons_id, CURRENT_DATE, 'OWNER');
    RETURN OLD;
  END IF;
END;
$$;

CREATE TRIGGER house_owner_change
AFTER INSERT OR DELETE ON houses_persons
FOR EACH ROW
EXECUTE FUNCTION house_owner_change_func();