CREATE OR REPLACE FUNCTION house_owner_change_func() RETURNS TRIGGER
LANGUAGE plpgsql AS
$$
BEGIN
  IF TG_OP = 'INSERT' THEN
    INSERT INTO house_history (house_id, person_id, date, person_type)
    VALUES (NEW.houses_id, NEW.persons_id, CURRENT_DATE, 'OWNER');
    RETURN NEW;
  ELSIF TG_OP = 'DELETE' THEN
    INSERT INTO house_history (house_id, person_id, date, person_type)
    VALUES (OLD.houses_id, OLD.persons_id, CURRENT_DATE, 'TENANT');
    RETURN OLD;
  END IF;
END;
$$;

CREATE TRIGGER house_owner_change
AFTER INSERT OR DELETE ON houses_persons
FOR EACH ROW
EXECUTE FUNCTION house_owner_change_func();