package ru.clevertec.house.service.impl;

import by.clevertec.exception.CheckEmptyException;
import by.clevertec.exception.NotFoundException;
import config.ServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.house.dto.request.PersonRequest;
import ru.clevertec.house.dto.response.PersonResponse;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.enums.TypePerson;
import ru.clevertec.house.mapper.PersonMapper;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.repository.PersonRepository;
import util.HouseTestData;
import util.PersonTestData;

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
import static util.initdata.TestDataForPerson.DEFAULT_PAGE_REQUEST_FOR_IT;
import static util.initdata.TestDataForPerson.PERSON_UUID;
import static util.initdata.TestDataForPerson.UPDATE_PERSON_NAME;
import static util.initdata.TestDataForPerson.UPDATE_PERSON_SURNAME;

@ServiceTest
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    private PersonServiceImpl personService;

    @Mock
    private PersonRepository personRepository;
    @Mock
    private HouseRepository houseRepository;

    private final PersonMapper personMapper;

    @Captor
    private ArgumentCaptor<Person> captor;

    @BeforeEach
    void setUp() {
        personService = new PersonServiceImpl(personRepository, houseRepository);
    }

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
            Page<PersonResponse> actual = personService.findAll(DEFAULT_PAGE_REQUEST_FOR_IT);

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
            Page<PersonResponse> actual = personService.findAll(DEFAULT_PAGE_REQUEST_FOR_IT);

            // then
            assertThat(actual).isEmpty();
        }

        @Test
        void shouldReturnListOfPersonsWhichLiveInHouse() {
            // given
            int expectedSize = 1;
            UUID houseUuid = HOUSE_UUID;
            PageRequest pageRequest = DEFAULT_PAGE_REQUEST_FOR_IT;
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
            PageRequest pageRequest = DEFAULT_PAGE_REQUEST_FOR_IT;

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
            PageRequest pageRequest = DEFAULT_PAGE_REQUEST_FOR_IT;

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
            PageRequest pageRequest = DEFAULT_PAGE_REQUEST_FOR_IT;

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

            Person expected = personMapper.toPerson(personRequest);

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
            assertThat(actual.getId()).isEqualTo(expected.getId());
            assertThat(actual.getName()).isEqualTo(expected.getName());
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

            Person personToUpdate = personMapper.toPerson(personRequest);

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
        public void testReturnThrowIfIncorrectFields() {
            // given
            UUID uuid = PERSON_UUID;

            Map<String, Object> fields = null;

            // when, then
            assertThrows(CheckEmptyException.class, () -> personService.patchUpdate(uuid, fields));
        }

        @Test
        public void testPatchUpdateWithNonExistentPerson() {
            // given
            UUID uuid = PERSON_UUID;

            Map<String, Object> fields = new HashMap<>();
            fields.put("name", "John");

            when(personRepository.findPersonByUuid(uuid)).thenReturn(Optional.empty());

            // when, then
            assertThrows(NotFoundException.class, () -> personService.patchUpdate(uuid, fields));
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