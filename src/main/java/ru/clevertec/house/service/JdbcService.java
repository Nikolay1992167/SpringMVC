package ru.clevertec.house.service;

import ru.clevertec.house.dto.response.HouseResponse;
import ru.clevertec.house.dto.response.PersonResponse;

import java.util.List;
import java.util.UUID;

public interface JdbcService {

    List<PersonResponse> findPersonWhichLiveInHouse(UUID houseId);

    List<PersonResponse> findPersonsFullTextSearch(String searchTerm);

    List<HouseResponse> findHousesWhichOwnPerson(UUID personId);

    List<HouseResponse> findHousesFullTextSearch(String searchTerm);
}
