package ru.clevertec.house.dao.impl;

import config.DataBaseConfigForDaoIT;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.clevertec.house.dao.HouseDao;
import ru.clevertec.house.entity.House;
import util.HouseTestData;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(DataBaseConfigForDaoIT.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class HouseDaoImplTest {

    private HouseDao houseDao;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    void setup() {
        houseDao = new HouseDaoImpl(sessionFactory);
    }

    @Test
    void findById() {
        // given
        UUID houseUUID = UUID.fromString("0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6");
        String expected = "Ельск";

        // when
        Optional<House> actual = houseDao.findById(houseUUID);

        //then
        actual.ifPresent(house ->
                assertThat(actual.get().getCity())
                        .isEqualTo(expected));
    }

    @Test
    void findAll() {
        // given
        int expectedSize = 4;
        int pageNumber = 1;
        int pageSize = 10;

        // when
        List<House> actual = houseDao.findAll(pageNumber, pageSize);

        // then
        assertThat(actual.size()).isEqualTo(expectedSize);
    }

    @Test
    void save() {
        // given
        House expected = HouseTestData.builder()
                .withId(null)
                .build().getEntity();

        // when
        House actual = houseDao.save(expected);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void update() {
        // given
        House expected = HouseTestData.builder()
                .withId(3L)
                .build().getEntity();

        // when
        House actual = houseDao.update(expected);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void delete() {
        // given
        UUID houseUUID = UUID.fromString("0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6");
        String expected = "Ельск";

        // when
        Optional<House> actual = houseDao.delete(houseUUID);

        // then
        actual.ifPresent(house ->
                assertThat(actual.get().getCity())
                        .isEqualTo(expected));
    }
}