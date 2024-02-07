package ru.clevertec.house.service.impl;

import by.clevertec.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.house.dto.request.PersonRequest;
import ru.clevertec.house.dto.response.PersonResponse;
import util.PostgresSqlContainerInitializer;
import util.initdata.TestDataForPerson;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static util.initdata.TestDataForHouse.HOUSE_UUID_FIFTH_IN_DB;
import static util.initdata.TestDataForHouse.HOUSE_UUID_FOURTH_IN_DB;
import static util.initdata.TestDataForHouse.HOUSE_UUID_THIRD_IN_DB;
import static util.initdata.TestDataForPerson.DEFAULT_PAGE_REQUEST_FOR_IT;
import static util.initdata.TestDataForPerson.PERSON_UUID_EIGHTH_IN_DB;
import static util.initdata.TestDataForPerson.PERSON_UUID_NINTH_IN_DB;
import static util.initdata.TestDataForPerson.PERSON_UUID_SECOND_IN_DB;
import static util.initdata.TestDataForPerson.PERSON_UUID_TENTH_IN_DB;
import static util.initdata.TestDataForPerson.UPDATE_PERSON_NAME;
import static util.initdata.TestDataForPerson.UPDATE_PERSON_SURNAME;

@RequiredArgsConstructor
public class PersonServiceImplIT extends PostgresSqlContainerInitializer {

    private final PersonServiceImpl personService;

    @Test
    void shouldReturnPersonResponseWhenFindById() {
        // given
        UUID personUuid = PERSON_UUID_SECOND_IN_DB;

        // when
        PersonResponse response = personService.findById(personUuid);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUuid()).isEqualTo(personUuid);
    }

    @Test
    void shouldReturnPageWithListOfPersonResponseWhenFindAll() {
        // given
        PageRequest pageRequest = DEFAULT_PAGE_REQUEST_FOR_IT;

        // when
        Page<PersonResponse> response = personService.findAll(pageRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty();
    }

    @Test
    void shouldReturnPageWithListOfPersonsWhichLiveInHouse() {
        // given
        UUID houseUuid = HOUSE_UUID_THIRD_IN_DB;
        PageRequest pageRequest = DEFAULT_PAGE_REQUEST_FOR_IT;
        int expectedSize = 3;

        // when
        Page<PersonResponse> response = personService.findPersonsWhichLiveInHouse(houseUuid, pageRequest);

        // then
        assertThat(response.getTotalElements()).isEqualTo(expectedSize);
    }

    @Test
    void shouldReturnPageWithListOfPersonsWhichSomeTimeLiveInHouse() {
        // given
        UUID houseUuid = HOUSE_UUID_FOURTH_IN_DB;
        PageRequest pageRequest = DEFAULT_PAGE_REQUEST_FOR_IT;
        int expectedSize = 2;

        // when
        Page<PersonResponse> response = personService.findPersonsWhichSomeTimeLiveInHouse(houseUuid, pageRequest);

        // then
        assertThat(response.getTotalElements()).isEqualTo(expectedSize);
    }

    @Test
    void shouldReturnPageWithListOfPersonsWhichSomeTimeOwnHouse() {
        // given
        UUID houseUuid = HOUSE_UUID_FIFTH_IN_DB;
        PageRequest pageRequest = DEFAULT_PAGE_REQUEST_FOR_IT;
        int expectedSize = 3;

        // when
        Page<PersonResponse> response = personService.findPersonsWhichSomeTimeOwnHouse(houseUuid, pageRequest);

        // then
        assertThat(response.getTotalElements()).isEqualTo(expectedSize);
    }

    @Test
    void shouldReturnPageWithListOfPersonResponseFindWithFullTextSearch() {
        // given
        String searchTerm = "ре";
        PageRequest pageRequest = DEFAULT_PAGE_REQUEST_FOR_IT;
        int expectedSize = 1;

        // when
        Page<PersonResponse> response = personService.findPersonsFullTextSearch(searchTerm, pageRequest);

        // then
        assertThat(response.getTotalElements()).isEqualTo(expectedSize);
    }

    @Test
    void shouldReturnPersonResponseWhenSaveInDB() {
        // given
        PersonRequest personToSave = TestDataForPerson.getNewPersonForCreate();

        // when
        PersonResponse savedPerson = personService.save(personToSave);

        // then
        assertThat(savedPerson.getUuid()).isNotNull();
        assertThat(savedPerson.getName()).isEqualTo(personToSave.getName());
        assertThat(savedPerson.getSurname()).isEqualTo(personToSave.getSurname());
        assertThat(savedPerson.getSex()).isEqualTo(personToSave.getSex());
        assertThat(savedPerson.getPassport()).isEqualTo(personToSave.getPassport());
        assertThat(savedPerson.getHouseUUID()).isEqualTo(personToSave.getHouseUUID());
    }

    @Test
    void shouldReturnUpdatedPersonResponseWhenUpdateInDB() {
        // given
        UUID personUuid = PERSON_UUID_NINTH_IN_DB;
        PersonRequest personToUpdate = TestDataForPerson.getPersonForUpdate();

        // when
        PersonResponse updatedPerson = personService.update(personUuid, personToUpdate);

        // then
        assertThat(updatedPerson.getUuid()).isEqualTo(personUuid);
        assertThat(updatedPerson.getName()).isEqualTo(personToUpdate.getName());
        assertThat(updatedPerson.getSurname()).isEqualTo(personToUpdate.getSurname());
        assertThat(updatedPerson.getSex()).isEqualTo(personToUpdate.getSex());
        assertThat(updatedPerson.getPassport()).isEqualTo(personToUpdate.getPassport());
        assertThat(updatedPerson.getHouseUUID()).isEqualTo(personToUpdate.getHouseUUID());
    }

    @Test
    void shouldReturnUpdatedPersonResponseWhenPatchUpdateInDB() {
        // given
        UUID personUuid = PERSON_UUID_TENTH_IN_DB;
        String name = UPDATE_PERSON_NAME;
        String surname = UPDATE_PERSON_SURNAME;
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", name);
        fields.put("surname", surname);

        // when
        PersonResponse patchUpdatedPerson = personService.patchUpdate(personUuid, fields);

        // then
        assertThat(patchUpdatedPerson.getUuid()).isEqualTo(personUuid);
        assertThat(patchUpdatedPerson.getName()).isEqualTo(name);
        assertThat(patchUpdatedPerson.getSurname()).isEqualTo(surname);
    }

    @Test
    void shouldDeletePersonInDB() {
        // given
        UUID personUuid = PERSON_UUID_EIGHTH_IN_DB;

        // when
        personService.delete(personUuid);

        // then
        assertThrows(NotFoundException.class, () -> personService.findById(personUuid));
    }
}