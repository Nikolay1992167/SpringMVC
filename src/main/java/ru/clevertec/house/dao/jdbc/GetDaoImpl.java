package ru.clevertec.house.dao.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.clevertec.house.dao.jdbc.rowmapper.HouseRowMapper;
import ru.clevertec.house.dao.jdbc.rowmapper.PersonRowMapper;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Person;

import java.util.List;
import java.util.UUID;

import static ru.clevertec.house.dao.util.SQLRequest.SQL_FOR_FIND_HOUSES;
import static ru.clevertec.house.dao.util.SQLRequest.SQL_FOR_FIND_PERSONS;
import static ru.clevertec.house.dao.util.SQLRequest.SQL_FOR_FULL_TEXT_SEARCH_HOUSES;
import static ru.clevertec.house.dao.util.SQLRequest.SQL_FOR_FULL_TEXT_SEARCH_PERSONS;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GetDaoImpl implements GetDao {

    private final JdbcTemplate jdbcTemplate;

    private final PersonRowMapper personRowMapper;

    private final HouseRowMapper houseRowMapper;


    @Override
    public List<Person> findPersonsWhichLiveInHouse(UUID houseId) {

        return jdbcTemplate.query(SQL_FOR_FIND_PERSONS, personRowMapper, houseId);
    }

    @Override
    public List<Person> findPersonsFullTextSearch(String searchTerm) {
        return jdbcTemplate.query(SQL_FOR_FULL_TEXT_SEARCH_PERSONS, personRowMapper, searchTerm);
    }

    @Override
    public List<House> findHousesWhichOwnPerson(UUID personId) {

        return jdbcTemplate.query(SQL_FOR_FIND_HOUSES, houseRowMapper, personId);
    }

    @Override
    public List<House> findHousesFullTextSearch(String searchTerm) {
        return jdbcTemplate.query(SQL_FOR_FULL_TEXT_SEARCH_HOUSES, houseRowMapper, searchTerm);
    }
}
