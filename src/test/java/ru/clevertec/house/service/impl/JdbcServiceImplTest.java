package ru.clevertec.house.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.house.dao.jdbc.GetDao;
import ru.clevertec.house.dto.response.HouseResponse;
import ru.clevertec.house.dto.response.PersonResponse;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Person;
import util.HouseTestData;
import util.PersonTestData;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static util.initdata.ConstantsForHouse.HOUSE_UUID;
import static util.initdata.ConstantsForPerson.PERSON_UUID;

@ExtendWith(MockitoExtension.class)
class JdbcServiceImplTest {

    @InjectMocks
    private JdbcServiceImpl jdbcService;

    @Mock
    private GetDao getDao;

    @Test
    void shouldReturnListOfPersonByHouseUUID() {
        // given
        UUID houseUuid = HOUSE_UUID;
        int expectedSize = 2;

        List<Person> personList = PersonTestData.builder()
                .build()
                .getListOfPerson();

        when(getDao.findPersonsWhichLiveInHouse(houseUuid))
                .thenReturn(personList);

        // when
        List<PersonResponse> actual = jdbcService.findPersonWhichLiveInHouse(houseUuid);

        // then
        assertThat(actual.size()).isEqualTo(expectedSize);
        verify(getDao, times(1)).findPersonsWhichLiveInHouse(houseUuid);
    }

    @Test
    void shouldReturnListOfPersonContainingSearchTerm() {
        // given
        String searchTerm = "ро";
        int expectedSize = 2;

        List<Person> personList = PersonTestData.builder()
                .build()
                .getListOfPerson();

        when(getDao.findPersonsFullTextSearch(searchTerm))
                .thenReturn(personList);

        // when
        List<PersonResponse> actual = jdbcService.findPersonsFullTextSearch(searchTerm);

        // then
        assertThat(actual.size()).isEqualTo(expectedSize);
        verify(getDao, times(1)).findPersonsFullTextSearch(searchTerm);
    }

    @Test
    void shouldReturnListOfHouseByPersonUUID() {
// given
        UUID personUuid = PERSON_UUID;
        int expectedSize = 2;

        List<House> personList = HouseTestData.builder()
                        .build().getListOfHouse();

        when(getDao.findHousesWhichOwnPerson(personUuid))
                .thenReturn(personList);

        // when
        List<HouseResponse> actual = jdbcService.findHousesWhichOwnPerson(personUuid);

        // then
        assertThat(actual.size()).isEqualTo(expectedSize);
        verify(getDao, times(1)).findHousesWhichOwnPerson(personUuid);
    }

    @Test
    void shouldReturnListOfHouseContainingSearchTerm() {
        // given
        String searchTerm = "лар";
        int expectedSize = 2;

        List<House> houseList = HouseTestData.builder()
                .build()
                .getListOfHouse();

        when(getDao.findHousesFullTextSearch(searchTerm))
                .thenReturn(houseList);

        // when
        List<HouseResponse> actual = jdbcService.findHousesFullTextSearch(searchTerm);

        // then
        assertThat(actual.size()).isEqualTo(expectedSize);
        verify(getDao, times(1)).findHousesFullTextSearch(searchTerm);
    }
}