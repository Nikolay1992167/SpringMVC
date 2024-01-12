package ru.clevertec.house.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.clevertec.house.dao.jdbc.GetDao;
import ru.clevertec.house.dto.response.HouseResponse;
import ru.clevertec.house.dto.response.PersonResponse;
import ru.clevertec.house.mapper.HouseMapper;
import ru.clevertec.house.mapper.PersonMapper;
import ru.clevertec.house.service.JdbcService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JdbcServiceImpl implements JdbcService {

    private final GetDao getDao;

    private final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);

    private final HouseMapper houseMapper = Mappers.getMapper(HouseMapper.class);

    @Override
    public List<PersonResponse> findPersonWhichLiveInHouse(UUID houseId) {

        List<PersonResponse> persons = getDao.findPersonsWhichLiveInHouse(houseId)
                .stream()
                .map(personMapper::toResponse)
                .peek(personResponse -> personResponse.setHouseUUID(houseId))
                .toList();
        log.info("Request completed findPersonWhichLiveInHouse, found: {}", persons.size());

        return new ArrayList<>(persons);
    }

    @Override
    public List<PersonResponse> findPersonsFullTextSearch(String searchTerm) {

        List<PersonResponse> persons = getDao.findPersonsFullTextSearch(searchTerm)
                .stream()
                .map(personMapper::toResponse)
                .toList();
        log.info("Request completed findPersonsFullTextSearch, found: {}", persons.size());

        return new ArrayList<>(persons);
    }

    @Override
    public List<HouseResponse> findHousesWhichOwnPerson(UUID personId) {

        List<HouseResponse> houses = getDao.findHousesWhichOwnPerson(personId)
                .stream()
                .map(houseMapper::toResponse)
                .toList();
        log.info("Request completed findHousesWhichOwnPerson, found: {}", houses.size());

        return new ArrayList<>(houses);
    }

    @Override
    public List<HouseResponse> findHousesFullTextSearch(String searchTerm) {

        List<HouseResponse> houses = getDao.findHousesFullTextSearch(searchTerm)
                .stream()
                .map(houseMapper::toResponse)
                .toList();
        log.info("Request completed findHousesFullTextSearch, found: {}", houses.size());

        return new ArrayList<>(houses);
    }
}
