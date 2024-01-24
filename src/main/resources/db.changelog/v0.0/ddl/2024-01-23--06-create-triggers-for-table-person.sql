CREATE OR REPLACE FUNCTION person_create_house_func() RETURNS TRIGGER
LANGUAGE plpgsql AS
$$
BEGIN
  INSERT INTO house_history (house_id, person_id, date, type_id)
  VALUES (NEW.house_id, NEW.id, CURRENT_DATE, 2);
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
  INSERT INTO house_history (house_id, person_id, date, type_id)
  VALUES (NEW.house_id, NEW.id, CURRENT_DATE, 2);
  RETURN NEW;
END;
$$;

CREATE TRIGGER person_change_house
AFTER UPDATE OF house_id ON persons
FOR EACH ROW
EXECUTE FUNCTION person_change_house_func();