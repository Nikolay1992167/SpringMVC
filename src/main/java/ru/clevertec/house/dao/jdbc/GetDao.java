package ru.clevertec.house.dao.jdbc;

import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Person;

import java.util.List;
import java.util.UUID;

public interface GetDao {

    /**
     * Find entity in repository
     *
     * @param houseId field uuid House
     * @return found entity if exist
     */
    List<Person> findPersonsWhichLiveInHouse(UUID houseId);

    /**
     * Find entity in repository
     *
     * @param personId field uuid Person
     * @return found entity if exist
     */
    List<House> findHousesWhichOwnPerson(UUID personId);
}
