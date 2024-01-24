CREATE OR REPLACE FUNCTION house_owner_change_func() RETURNS TRIGGER
LANGUAGE plpgsql AS
$$
BEGIN
  IF TG_OP = 'INSERT' THEN
    INSERT INTO house_history (house_id, person_id, date, type_id)
    VALUES (NEW.houses_id, NEW.persons_id, CURRENT_DATE, 1);
    RETURN NEW;
  ELSIF TG_OP = 'DELETE' THEN
    INSERT INTO house_history (house_id, person_id, date, type_id)
    VALUES (OLD.houses_id, OLD.persons_id, CURRENT_DATE, 2);
    RETURN OLD;
  END IF;
END;
$$;

CREATE TRIGGER house_owner_change
AFTER INSERT OR DELETE ON houses_persons
FOR EACH ROW
EXECUTE FUNCTION house_owner_change_func();