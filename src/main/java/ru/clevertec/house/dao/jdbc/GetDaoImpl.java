package ru.clevertec.house.dao.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.clevertec.house.dao.jdbc.rowmapper.HouseRowMapper;
import ru.clevertec.house.dao.jdbc.rowmapper.PersonRowMapper;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Person;

import java.util.List;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GetDaoImpl implements GetDao{

    private final JdbcTemplate jdbcTemplate;

    private final PersonRowMapper personRowMapper;

    private final HouseRowMapper houseRowMapper;

    private static final String SQL_FOR_FIND_PERSONS = "SELECT * FROM persons WHERE house_id = (SELECT id FROM houses WHERE uuid = ?)";
    private static final String SQL_FOR_FIND_HOUSES = "SELECT * FROM houses h JOIN houses_persons hp ON h.id = hp.houses_id JOIN persons p ON hp.persons_id = p.id WHERE p.uuid = ?";

    @Override
    public List<Person> findPersonsWhichLiveInHouse(UUID houseId) {

        return jdbcTemplate.query(SQL_FOR_FIND_PERSONS, personRowMapper, houseId);
    }

    @Override
    public List<House> findHousesWhichOwnPerson(UUID personId) {

        return jdbcTemplate.query(SQL_FOR_FIND_HOUSES, houseRowMapper, personId);
    }
}
