package by.clevertec.dao;

import by.clevertec.entity.Person;

import java.util.List;
import java.util.UUID;

public interface PersonDao extends CrudDao<Person, UUID>{

    List<Person> findAllPersonWhenLiveInHouse(UUID houseId);
}
