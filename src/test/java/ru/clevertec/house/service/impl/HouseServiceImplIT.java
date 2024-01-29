package ru.clevertec.house.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.house.dto.request.HouseRequest;
import ru.clevertec.house.dto.response.HouseResponse;
import ru.clevertec.house.exception.NotFoundException;
import util.PostgresSqlContainerInitializer;
import util.initdata.TestDataForHouse;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static util.initdata.TestDataForHouse.HOUSE_UUID_FIRST_IN_DB;
import static util.initdata.TestDataForHouse.HOUSE_UUID_FOURTH_IN_DB;
import static util.initdata.TestDataForHouse.HOUSE_UUID_SECOND_IN_DB;
import static util.initdata.TestDataForHouse.HOUSE_UUID_THIRD_IN_DB;
import static util.initdata.TestDataForHouse.UPDATE_HOUSE_AREA;
import static util.initdata.TestDataForHouse.UPDATE_HOUSE_NUMBER;
import static util.initdata.TestDataForPerson.DEFAULT_PAGE_REQUEST_FOR_IT;
import static util.initdata.TestDataForPerson.PERSON_UUID_FIRST_IN_DB;
import static util.initdata.TestDataForPerson.PERSON_UUID_FOURTH_IN_DB;

@RequiredArgsConstructor
public class HouseServiceImplIT extends PostgresSqlContainerInitializer {

    private final HouseServiceImpl houseService;

    @Test
    void shouldReturnHouseResponseWhenFindById() {
        // given
        UUID houseUuid = HOUSE_UUID_FIRST_IN_DB;

        // when
        HouseResponse response = houseService.findById(houseUuid);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUuid()).isEqualTo(houseUuid);
    }

    @Test
    void shouldReturnPageWithListOfHouseResponseWhenFindAll() {
        // given
        PageRequest pageRequest = DEFAULT_PAGE_REQUEST_FOR_IT;

        // when
        Page<HouseResponse> response = houseService.findAll(pageRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty();
    }

    @Test
    void shouldReturnPageWithListOfHouseResponseWhichSomeTimeLivesPerson() {
        // given
        UUID personUuid = PERSON_UUID_FIRST_IN_DB;
        PageRequest pageRequest = DEFAULT_PAGE_REQUEST_FOR_IT;
        int expectedSize = 1;

        // when
        Page<HouseResponse> response = houseService.findHousesWhichSomeTimeLivesPerson(personUuid, pageRequest);

        // then
        assertThat(response.getTotalElements()).isEqualTo(expectedSize);
    }

    @Test
    void shouldReturnPageWithListOfHouseResponseWhichOwnPerson() {
        // given
        UUID personUuid = PERSON_UUID_FIRST_IN_DB;
        PageRequest pageRequest = DEFAULT_PAGE_REQUEST_FOR_IT;
        int expectedSize = 1;

        // when
        Page<HouseResponse> response = houseService.findHousesWhichOwnPerson(personUuid, pageRequest);

        // then
        assertThat(response.getTotalElements()).isEqualTo(expectedSize);
    }

    @Test
    void shouldReturnPageWithListOfHouseResponseWhichSomeTimeOwnPerson() {
        // given
        UUID personUuid = PERSON_UUID_FOURTH_IN_DB;
        PageRequest pageRequest = DEFAULT_PAGE_REQUEST_FOR_IT;
        int expectedSize = 1;

        // when
        Page<HouseResponse> response = houseService.findHousesWhichSomeTimeOwnPerson(personUuid, pageRequest);

        // then
        assertThat(response.getTotalElements()).isEqualTo(expectedSize);
    }

    @Test
    void shouldReturnPageWithListOfHouseResponseFindWithFullTextSearch() {
        // given
        String searchTerm = "ин";
        PageRequest pageRequest = DEFAULT_PAGE_REQUEST_FOR_IT;
        int expectedSize = 4;

        // when
        Page<HouseResponse> response = houseService.findHousesFullTextSearch(searchTerm, pageRequest);

        // then
        assertThat(response.getTotalElements()).isEqualTo(expectedSize);
    }

    @Test
    void shouldReturnHouseResponseWhenSaveInDB() {
        // given
        HouseRequest houseToSave = TestDataForHouse.getNewHouseForCreate();

        // when
        HouseResponse savedHouse = houseService.save(houseToSave);

        // then
        assertThat(savedHouse.getUuid()).isNotNull();
        assertThat(savedHouse.getArea()).isEqualTo(houseToSave.getArea());
        assertThat(savedHouse.getCountry()).isEqualTo(houseToSave.getCountry());
        assertThat(savedHouse.getCity()).isEqualTo(houseToSave.getCity());
        assertThat(savedHouse.getStreet()).isEqualTo(houseToSave.getStreet());
        assertThat(savedHouse.getNumber()).isEqualTo(houseToSave.getNumber());
    }

    @Test
    void shouldReturnUpdatedHouseResponseWhenUpdateInDB() {
        // given
        UUID houseUuid = HOUSE_UUID_FOURTH_IN_DB;
        HouseRequest houseToUpdate = TestDataForHouse.getHouseForUpdate();

        // when
        HouseResponse updatedHouse = houseService.update(houseUuid, houseToUpdate);

        // then
        assertThat(updatedHouse.getUuid()).isEqualTo(houseUuid);
        assertThat(updatedHouse.getArea()).isEqualTo(houseToUpdate.getArea());
        assertThat(updatedHouse.getCountry()).isEqualTo(houseToUpdate.getCountry());
        assertThat(updatedHouse.getCity()).isEqualTo(houseToUpdate.getCity());
        assertThat(updatedHouse.getStreet()).isEqualTo(houseToUpdate.getStreet());
        assertThat(updatedHouse.getNumber()).isEqualTo(houseToUpdate.getNumber());
    }

    @Test
    void shouldReturnUpdatedHouseResponseWhenPatchUpdateInDB() {
        // given
        UUID houseUuid = HOUSE_UUID_THIRD_IN_DB;
        String area = UPDATE_HOUSE_AREA;
        int number = UPDATE_HOUSE_NUMBER;
        Map<String, Object> fields = new HashMap<>();
        fields.put("area", area);
        fields.put("number", number);

        // when
        HouseResponse patchUpdatedHouse = houseService.patchUpdate(houseUuid, fields);

        // then
        assertThat(patchUpdatedHouse.getUuid()).isEqualTo(houseUuid);
        assertThat(patchUpdatedHouse.getArea()).isEqualTo(area);
        assertThat(patchUpdatedHouse.getNumber()).isEqualTo(number);
    }

    @Test
    void shouldDeleteHouseInDB() {
        // given
        UUID houseUuid = HOUSE_UUID_SECOND_IN_DB;

        // when
        houseService.delete(houseUuid);

        // then
        assertThrows(NotFoundException.class, () -> houseService.findById(houseUuid));

    }
}