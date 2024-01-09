package by.clevertec.dao;

import by.clevertec.entity.Person;

import java.util.List;

public interface PersonDao extends CrudDao<Person, Long>{

    List<Person> findAllPersonWhenLiveInHouse(Long houseId);
}
