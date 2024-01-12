package ru.clevertec.house.service;

import ru.clevertec.house.dto.response.HouseResponse;
import ru.clevertec.house.dto.response.PersonResponse;

import java.util.List;
import java.util.UUID;

public interface JdbcService {

    public List<PersonResponse> findPersonWhichLiveInHouse(UUID houseId);

    public List<HouseResponse> findHousesWhichOwnPerson(UUID personId);
}
