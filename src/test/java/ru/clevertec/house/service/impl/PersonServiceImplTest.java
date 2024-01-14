package ru.clevertec.house.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.house.dao.HouseDao;
import ru.clevertec.house.dao.PersonDao;
import ru.clevertec.house.dto.request.PersonRequest;
import ru.clevertec.house.dto.response.PersonResponse;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.mapper.PersonMapper;
import util.HouseTestData;
import util.PersonTestData;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static util.initdata.ConstantsForHouse.HOUSE_UUID;
import static util.initdata.ConstantsForHouse.INCORRECT_UUID;
import static util.initdata.ConstantsForPerson.PERSON_UUID;
import static util.initdata.ConstantsForPerson.UPDATE_PERSON_NAME;
import static util.initdata.ConstantsForPerson.UPDATE_PERSON_SURNAME;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    @InjectMocks
    private PersonServiceImpl personService;

    @Mock
    private PersonDao personDao;

    @Mock
    private HouseDao houseDao;

    private final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);

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

            when(personDao.findById(personUuid))
                    .thenReturn(optionalPerson);

            // when
            PersonResponse actual = personService.findById(personUuid);

            // then
            assertThat(actual).isEqualTo(expected);
            verify(personDao, times(1)).findById(personUuid);
        }

        @Test
        void shouldReturnThrowIfPersonNotExistWithUUID() {
            // given
            UUID incorrectUuid = INCORRECT_UUID;

            Optional<Person> optionalPerson = Optional.empty();

            when(personDao.findById(incorrectUuid))
                    .thenReturn(optionalPerson);

            // when, then
            assertThrows(NotFoundException.class, () -> personService.findById(incorrectUuid));
            verify(personDao, times(1)).findById(incorrectUuid);
        }
    }

    @Nested
    class FindAll {

        @Test
        void shouldReturnListOfPersonResponse() {
            // given
            int expectedSize = 1;
            int pageNumber = 1;
            int pageSize = 5;

            List<Person> personList = List.of(PersonTestData.builder()
                    .build()
                    .getEntity());

            when(personDao.findAll(pageNumber, pageSize))
                    .thenReturn(personList);

            // when
            List<PersonResponse> actual = personService.findAll(pageNumber, pageSize);

            // then
            assertThat(actual.size()).isEqualTo(expectedSize);
        }

        @Test
        void shouldCheckEmpty() {
            // given
            int pageNumber = 1;
            int pageSize = 10;

            when(personDao.findAll(pageNumber, pageSize))
                    .thenReturn(List.of());

            // when
            List<PersonResponse> actual = personService.findAll(pageNumber, pageSize);

            // then
            assertThat(actual).isEmpty();
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

            when(houseDao.findById(personRequest.getHouseUUID()))
                    .thenReturn(houseInDB);

            expected.setHouse(houseInDB.get());

            when(personDao.save(expected)).thenReturn(person);

            // when
            personService.save(personRequest);

            // then
            verify(personDao, times(1)).save(captor.capture());
            Person actual = captor.getValue();
            assertThat(actual).isEqualTo(expected);
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

            when(houseDao.findById(houseUUID)).thenReturn(houseInDB);
            when(personDao.findById(personUuid)).thenReturn(personInDB);

            personToUpdate.setId(personInDB.get().getId());
            personToUpdate.setUuid(personInDB.get().getUuid());
            personToUpdate.setHouse(houseInDB.get());
            personToUpdate.setCreateDate(personInDB.get().getCreateDate());

            when(personDao.update(personToUpdate)).thenReturn(updatedPerson);

            // when
            PersonResponse actual = personService.update(personUuid, personRequest);

            // then
            assertThat(actual).isEqualTo(expected);
            verify(houseDao, times(1)).findById(houseUUID);
            verify(personDao, times(1)).findById(personUuid);
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

            when(houseDao.findById(houseUUID)).thenReturn(houseInDB);
            when(personDao.findById(incorrectUUID)).thenReturn(Optional.empty());

            // when, then
            assertThrows(NotFoundException.class, () -> personService.update(incorrectUUID, personRequest));
            verify(personDao, times(1)).findById(incorrectUUID);
            verify(personDao, never()).update(any(Person.class));
            verify(houseDao, times(1)).findById(houseUUID);
        }
    }

    @Nested
    class Delete {

        @Test
        void shouldDeletePersonByUUID() {
            // given
            UUID personUuid = PERSON_UUID;

            Optional<Person> personInDB = PersonTestData.builder()
                    .build()
                    .getOptionalEntity();

            when(personDao.delete(personUuid)).thenReturn(personInDB);

            // when
            personService.delete(personUuid);

            // then
            verify(personDao, times(1)).delete(personUuid);
        }

        @Test
        void shouldReturnThrowIfHouseNotExistWithUUID() {
            // given
            UUID incorrectUUID = INCORRECT_UUID;

            // when, then
            assertThrows(NotFoundException.class, () -> personService.delete(incorrectUUID));
            verify(personDao, times(1)).delete(incorrectUUID);
        }
    }
}