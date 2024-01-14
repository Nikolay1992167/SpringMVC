package ru.clevertec.house.dao.jdbc;

import config.DataBaseConfigForDaoIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.clevertec.house.dao.jdbc.rowmapper.HouseRowMapper;
import ru.clevertec.house.dao.jdbc.rowmapper.PersonRowMapper;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Person;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(DataBaseConfigForDaoIT.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class GetDaoImplTest {

    private GetDao getDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        getDao = new GetDaoImpl(jdbcTemplate,
                new PersonRowMapper(),
                new HouseRowMapper());
    }

    @Test
    void shouldReturnListOfPersonWhichLiveInHouse() {
        // given
        UUID uuidHouse = UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13");
        int expectedSize = 3;

        // when
        List<Person> actual = getDao.findPersonsWhichLiveInHouse(uuidHouse);

        // then
        assertThat(actual.size()).isEqualTo(expectedSize);
    }

    @Test
    void shouldReturnEmptyListIfNotPersonWhichLiveInHouseWithUUID() {
        // given
        UUID uuidHouse = UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a54");

        // when
        List<Person> actual = getDao.findPersonsWhichLiveInHouse(uuidHouse);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnListOfPersonsFullTextSearch() {
        // given
        int expectedSize = 2;
        String searchTerm = "ен";

        // when
        List<Person> actual = getDao.findPersonsFullTextSearch(searchTerm);

        // then
        assertThat(actual.size()).isEqualTo(expectedSize);
    }

    @Test
    void shouldReturnEmptyListIfNotPersonsWithSearchTerm() {
        // given
        String searchTerm = "ротан";

        // when
        List<Person> actual = getDao.findPersonsFullTextSearch(searchTerm);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnListOfHouseWhichOwnPerson() {
        // given
        UUID personUuid = UUID.fromString("63a1faca-a963-4d4b-bfb9-2dafaedc36fe");
        int expectedSize = 1;

        // when
        List<House> actual = getDao.findHousesWhichOwnPerson(personUuid);

        // then
        assertThat(actual.size()).isEqualTo(expectedSize);
    }

    @Test
    void shouldReturnEmptyListIfNotHouseWhichOwnPersonWithUUID() {
        // given
        UUID personUuid = UUID.fromString("922e0213-e543-48ef-b8cb-92592afd5999");

        // when
        List<House> actual = getDao.findHousesWhichOwnPerson(personUuid);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnListOfHousesFullTextSearch() {
        // given
        int expectedSize = 2;
        String searchTerm = "вич";

        // when
        List<House> actual = getDao.findHousesFullTextSearch(searchTerm);

        // then
        assertThat(actual.size()).isEqualTo(expectedSize);
    }

    @Test
    void shouldReturnEmptyListIfNotHouseWithSearchTerm() {
        // given
        String searchTerm = "бараб";

        // when
        List<House> actual = getDao.findHousesFullTextSearch(searchTerm);

        // then
        assertThat(actual).isEmpty();
    }
}