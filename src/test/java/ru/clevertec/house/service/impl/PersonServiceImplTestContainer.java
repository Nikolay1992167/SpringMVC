package ru.clevertec.house.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.house.dto.request.PersonRequest;
import ru.clevertec.house.dto.response.PersonResponse;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.enums.Sex;
import ru.clevertec.house.enums.TypePerson;
import ru.clevertec.house.exception.CheckEmptyException;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.repository.PersonRepository;
import util.HouseTestData;
import util.PersonTestData;
import util.PostgresSqlContainerInitializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static util.initdata.TestDataForHouse.HOUSE_UUID;
import static util.initdata.TestDataForHouse.INCORRECT_UUID;
import static util.initdata.TestDataForPerson.PERSON_UUID;
import static util.initdata.TestDataForPerson.UPDATE_PERSON_NAME;
import static util.initdata.TestDataForPerson.UPDATE_PERSON_SURNAME;

@RequiredArgsConstructor
public class PersonServiceImplTestContainer extends PostgresSqlContainerInitializer {

    private final PersonServiceImpl personService;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private HouseRepository houseRepository;

    @Captor
    private ArgumentCaptor<Person> captor;

    @Nested
    class FindById {

        @Test
        void shouldReturnExpectedPersonByUUID() {
            // given
            UUID personUuid = PERSON_UUID;

            Optional<Person> optionalPerson = PersonTestData.builder()
                    .build()
                    .getOptionalEntity();

            PersonResponse expected = PersonTestData.builder()
                    .build()
                    .getResponseDto();

            when(personRepository.findPersonByUuid(personUuid))
                    .thenReturn(optionalPerson);

            // when
            PersonResponse actual = personService.findById(personUuid);

            // then
            assertThat(actual).isEqualTo(expected);
            verify(personRepository, times(1)).findPersonByUuid(personUuid);
        }

        @Test
        void shouldReturnThrowIfPersonNotExistWithUUID() {
            // given
            UUID incorrectUuid = INCORRECT_UUID;

            Optional<Person> optionalPerson = Optional.empty();

            when(personRepository.findPersonByUuid(incorrectUuid))
                    .thenReturn(optionalPerson);

            // when, then
            assertThrows(NotFoundException.class, () -> personService.findById(incorrectUuid));
            verify(personRepository, times(1)).findPersonByUuid(incorrectUuid);
        }
    }

    @Nested
    class FindPersons {

        @Test
        void shouldReturnListOfPersonResponse() {
            // given
            int expectedSize = 1;
            List<Person> personList = List.of(PersonTestData.builder()
                    .build()
                    .getEntity());
            Page<Person> page = new PageImpl<>(personList);

            when(personRepository.findAll(any(PageRequest.class)))
                    .thenReturn(page);

            // when
            Page<PersonResponse> actual = personService.findAll(PageRequest.of(0, 15));

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
            verify(personRepository, times(1)).findAll(any(PageRequest.class));
        }

        @Test
        void shouldCheckEmpty() {
            // given
            Page<Person> page = new PageImpl<>(List.of());

            when(personRepository.findAll(any(PageRequest.class)))
                    .thenReturn(page);

            // when
            Page<PersonResponse> actual = personService.findAll(PageRequest.of(0, 15));

            // then
            assertThat(actual).isEmpty();
        }

        @Test
        void shouldReturnListOfPersonsWhichLiveInHouse() {
            // given
            int expectedSize = 1;
            UUID houseUuid = HOUSE_UUID;
            PageRequest pageRequest = PageRequest.of(0, 15);
            List<Person> personList = List.of(PersonTestData.builder()
                    .build()
                    .getEntity());

            Page<Person> page = new PageImpl<>(personList);

            when(personRepository.findAllByHouseUuid(houseUuid, pageRequest))
                    .thenReturn(page);

            // when
            Page<PersonResponse> actual = personService.findPersonsWhichLiveInHouse(houseUuid, pageRequest);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
            verify(personRepository, times(1)).findAllByHouseUuid(houseUuid, pageRequest);
        }

        @Test
        void shouldReturnListOfPersonsWhichSomeTimeLiveInHouse() {
            // given
            int expectedSize = 1;
            UUID houseUuid = HOUSE_UUID;
            TypePerson typePerson = TypePerson.TENANT;
            PageRequest pageRequest = PageRequest.of(0, 15);

            List<Person> personList = List.of(PersonTestData.builder()
                    .build()
                    .getEntity());

            Page<Person> page = new PageImpl<>(personList);

            when(personRepository.findByPersonHouseHistoriesHouseUuidAndPersonHouseHistoriesType(houseUuid, typePerson, pageRequest))
                    .thenReturn(page);

            // when
            Page<PersonResponse> actual = personService.findPersonsWhichSomeTimeLiveInHouse(houseUuid, pageRequest);

            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
            verify(personRepository, times(1))
                    .findByPersonHouseHistoriesHouseUuidAndPersonHouseHistoriesType(houseUuid, typePerson, pageRequest);
        }

        @Test
        void shouldReturnListOfPersonsWhichSomeTimeOwnHouse() {
            // given
            int expectedSize = 1;
            UUID houseUuid = HOUSE_UUID;
            TypePerson typePerson = TypePerson.OWNER;
            PageRequest pageRequest = PageRequest.of(0, 15);

            List<Person> personList = List.of(PersonTestData.builder()
                    .build()
                    .getEntity());

            Page<Person> page = new PageImpl<>(personList);

            when(personRepository.findByPersonHouseHistoriesHouseUuidAndPersonHouseHistoriesType(houseUuid, typePerson, pageRequest))
                    .thenReturn(page);

            // when
            Page<PersonResponse> actual = personService.findPersonsWhichSomeTimeOwnHouse(houseUuid, pageRequest);

            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
            verify(personRepository, times(1))
                    .findByPersonHouseHistoriesHouseUuidAndPersonHouseHistoriesType(houseUuid, typePerson, pageRequest);
        }

        @Test
        void shouldReturnListOfPersonsFindWithFullTextSearch() {
            // given
            int expectedSize = 1;
            String searchTerm = "ро";
            PageRequest pageRequest = PageRequest.of(0, 15);

            List<Person> personList = List.of(PersonTestData.builder()
                    .build()
                    .getEntity());

            Page<Person> page = new PageImpl<>(personList);

            when(personRepository.findPersonsFullTextSearch(searchTerm, pageRequest))
                    .thenReturn(page);

            // when
            Page<PersonResponse> actual = personService.findPersonsFullTextSearch(searchTerm, pageRequest);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
            verify(personRepository, times(1)).findPersonsFullTextSearch(searchTerm, pageRequest);
        }
    }

    @Nested
    class Save {

        @Test
        void shouldReturnSavedPersonResponseIfValidPersonRequest() {
            // given
            PersonRequest personRequest = PersonTestData.builder()
                    .build()
                    .getRequestDto();

            Person expected = PersonTestData.builder()
                    .build()
                    .getEntity();

            Person person = PersonTestData.builder()
                    .build()
                    .getEntity();

            Optional<House> houseInDB = HouseTestData.builder()
                    .build()
                    .getOptionalEntity();

            when(houseRepository.findHouseByUuid(personRequest.getHouseUUID()))
                    .thenReturn(houseInDB);

            expected.setHouse(houseInDB.get());

            when(personRepository.save(any(Person.class)))
                    .thenReturn(person);

            // when
            personService.save(personRequest);

            // then
            verify(personRepository, times(1)).save(captor.capture());
            Person actual = captor.getValue();
            assertThat(actual.getName()).isEqualTo(expected.getName());
            assertThat(actual.getSurname()).isEqualTo(expected.getSurname());
            assertThat(actual.getSex()).isEqualTo(expected.getSex());
            assertThat(actual.getHouse()).isEqualTo(expected.getHouse());
        }
    }

    @Nested
    class Update {

        @Test
        void shouldReturnUpdatedPersonResponseIfValidPersonRequest() {
            // given
            UUID personUuid = PERSON_UUID;
            UUID houseUUID = HOUSE_UUID;

            PersonRequest personRequest = PersonTestData.builder()
                    .withName(UPDATE_PERSON_NAME)
                    .withSurname(UPDATE_PERSON_SURNAME)
                    .build()
                    .getRequestDto();

            Person personToUpdate = PersonTestData.builder()
                    .withName(UPDATE_PERSON_NAME)
                    .withSurname(UPDATE_PERSON_SURNAME)
                    .build()
                    .getEntity();

            Optional<Person> personInDB = PersonTestData.builder()
                    .build()
                    .getOptionalEntity();

            Optional<House> houseInDB = HouseTestData.builder()
                    .build()
                    .getOptionalEntity();

            Person updatedPerson = PersonTestData.builder()
                    .withName(UPDATE_PERSON_NAME)
                    .withSurname(UPDATE_PERSON_SURNAME)
                    .build()
                    .getEntity();

            PersonResponse expected = PersonTestData.builder()
                    .withName(UPDATE_PERSON_NAME)
                    .withSurname(UPDATE_PERSON_SURNAME)
                    .build()
                    .getResponseDto();

            when(houseRepository.findHouseByUuid(houseUUID))
                    .thenReturn(houseInDB);
            when(personRepository.findPersonByUuid(personUuid))
                    .thenReturn(personInDB);

            personToUpdate.setId(personInDB.get().getId());
            personToUpdate.setUuid(personInDB.get().getUuid());
            personToUpdate.setHouse(houseInDB.get());
            personToUpdate.setCreateDate(personInDB.get().getCreateDate());

            when(personRepository.save(any(Person.class)))
                    .thenReturn(updatedPerson);

            // when
            PersonResponse actual = personService.update(personUuid, personRequest);

            // then
            assertThat(actual).isEqualTo(expected);
            verify(houseRepository, times(1)).findHouseByUuid(houseUUID);
            verify(personRepository, times(1)).findPersonByUuid(personUuid);
        }

        @Test
        void shouldReturnThrowIfPersonNotExistWithUUID() {
            // given
            UUID incorrectUUID = INCORRECT_UUID;
            UUID houseUUID = HOUSE_UUID;

            PersonRequest personRequest = PersonTestData.builder()
                    .withName(UPDATE_PERSON_NAME)
                    .withSurname(UPDATE_PERSON_SURNAME)
                    .build()
                    .getRequestDto();

            Optional<House> houseInDB = HouseTestData.builder()
                    .build()
                    .getOptionalEntity();

            when(houseRepository.findHouseByUuid(houseUUID))
                    .thenReturn(houseInDB);
            when(personRepository.findPersonByUuid(incorrectUUID))
                    .thenReturn(Optional.empty());

            // when, then
            assertThrows(NotFoundException.class, () -> personService.update(incorrectUUID, personRequest));
            verify(personRepository, times(1)).findPersonByUuid(incorrectUUID);
            verify(personRepository, never()).save(any(Person.class));
            verify(houseRepository, times(1)).findHouseByUuid(houseUUID);
        }
    }

    @Nested
    class PatchUpdate {

        @Test
        void shouldReturnUpdatedPatchPersonResponseIfValidFields() {
            // given
            UUID personUuid = PERSON_UUID;

            Map<String, Object> fieldsToUpdate = new HashMap<>();
            fieldsToUpdate.put("name", UPDATE_PERSON_NAME);
            fieldsToUpdate.put("surname", UPDATE_PERSON_SURNAME);

            Optional<Person> personInDB = PersonTestData.builder()
                    .build()
                    .getOptionalEntity();

            Person patchedPerson = PersonTestData.builder()
                    .withName(UPDATE_PERSON_NAME)
                    .withSurname(UPDATE_PERSON_SURNAME)
                    .build()
                    .getEntity();

            PersonResponse expected = PersonTestData.builder()
                    .withName(UPDATE_PERSON_NAME)
                    .withSurname(UPDATE_PERSON_SURNAME)
                    .build()
                    .getResponseDto();

            when(personRepository.findPersonByUuid(personUuid))
                    .thenReturn(personInDB);

            when(personRepository.save(any(Person.class)))
                    .thenReturn(patchedPerson);

            // when
            PersonResponse actual = personService.patchUpdate(personUuid, fieldsToUpdate);

            // then
            assertThat(actual).isEqualTo(expected);
            verify(personRepository, times(1)).findPersonByUuid(personUuid);
        }

        @Test
        void shouldReturnThrowIfIncorrectDataInParametersForPatchUpdate() {
            // given
            UUID personUuid = INCORRECT_UUID;

            Map<String, Object> fieldsToUpdate = new HashMap<>();
            fieldsToUpdate.put("name", "");
            fieldsToUpdate.put("surname", "");

            // when, then
            assertThrows(CheckEmptyException.class, () -> personService.patchUpdate(personUuid, fieldsToUpdate));
            verify(personRepository, never()).save(any(Person.class));
        }

        @Test
        void shouldReturnThrowIfPersonNotExistWithUUID() {
            // given
            UUID personUuid = INCORRECT_UUID;

            Map<String, Object> fieldsToUpdate = new HashMap<>();
            fieldsToUpdate.put("name", UPDATE_PERSON_NAME);
            fieldsToUpdate.put("surname", UPDATE_PERSON_SURNAME);

            when(personRepository.findPersonByUuid(personUuid))
                    .thenReturn(Optional.empty());

            // when, then
            assertThrows(NotFoundException.class, () -> personService.patchUpdate(personUuid, fieldsToUpdate));
            verify(personRepository, times(1)).findPersonByUuid(personUuid);
            verify(personRepository, never()).save(any(Person.class));
        }

        @Test
        void shouldSetSexFieldIfTypeIsSex() {
            // given
            UUID personUuid = PERSON_UUID;

            Map<String, Object> fields = new HashMap<>();
            fields.put("sex", "MALE");

            Person person = PersonTestData.builder()
                    .build()
                    .getEntity();
            person.setSex(Sex.FEMALE);

            when(personRepository.findPersonByUuid(personUuid)).thenReturn(Optional.of(person));

            // when
            personService.patchUpdate(personUuid, fields);

            // then
            verify(personRepository).save(captor.capture());
            Person updatedPerson = captor.getValue();
            assertThat(updatedPerson.getSex()).isEqualTo(Sex.MALE);
        }
    }

    @Nested
    class Delete {

        @Test
        void shouldDeletePersonByUUID() {
            // given
            UUID personUuid = PERSON_UUID;

            Optional<Person> optionalEntity = PersonTestData.builder()
                    .build()
                    .getOptionalEntity();

            when(personRepository.findPersonByUuid(personUuid))
                    .thenReturn(optionalEntity);

            doNothing().when(personRepository).deletePersonByUuid(personUuid);

            // when
            personService.delete(personUuid);

            // then
            verify(personRepository, times(1)).findPersonByUuid(personUuid);
            verify(personRepository, times(1)).deletePersonByUuid(personUuid);
        }

        @Test
        void shouldReturnThrowIfHouseNotExistWithUUID() {
            // given
            UUID incorrectUUID = INCORRECT_UUID;

            // when, then
            assertThrows(NotFoundException.class, () -> personService.delete(incorrectUUID));
            verify(personRepository, never()).deletePersonByUuid(incorrectUUID);
        }
    }
}
