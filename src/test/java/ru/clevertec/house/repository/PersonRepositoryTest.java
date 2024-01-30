package ru.clevertec.house.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.enums.TypePerson;
import util.PostgresSqlContainerInitializer;
import util.initdata.TestDataForPerson;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static util.initdata.TestDataForHouse.HOUSE_UUID_FOURTH_IN_DB;
import static util.initdata.TestDataForHouse.HOUSE_UUID_THIRD_IN_DB;
import static util.initdata.TestDataForHouse.INCORRECT_UUID;
import static util.initdata.TestDataForPerson.PERSON_UUID;
import static util.initdata.TestDataForPerson.PERSON_UUID_TENTH_IN_DB;

@RequiredArgsConstructor
class PersonRepositoryTest extends PostgresSqlContainerInitializer {

    private final PersonRepository personRepository;

    final PageRequest request = PageRequest.of(0, 5);

    @Nested
    class FindByUuid {

        @Test
        void shouldReturnExpectedValue() {
            // given
            Person expected = TestDataForPerson.getFirstPersonInDB();

            // when
            Person actual = personRepository.findPersonByUuid(expected.getUuid()).get();

            // then
            assertThat(actual.getId()).isEqualTo(expected.getId());
            assertThat(actual.getUuid()).isEqualTo(expected.getUuid());
            assertThat(actual.getName()).isEqualTo(expected.getName());
            assertThat(actual.getSurname()).isEqualTo(expected.getSurname());
            assertThat(actual.getSex()).isEqualTo(expected.getSex());
            assertThat(actual.getPassport()).isEqualTo(expected.getPassport());
            assertThat(actual.getCreateDate()).isEqualTo(expected.getCreateDate());
            assertThat(actual.getUpdateDate()).isEqualTo(expected.getUpdateDate());
            assertThat(actual.getHouse().getId()).isEqualTo(expected.getHouse().getId());
        }

        @Test
        void shouldReturnEmptyOptional() {
            // given
            UUID personUuid = PERSON_UUID;

            // when
            Optional<Person> actual = personRepository.findPersonByUuid(personUuid);

            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class FindAllByHouseUuid {

        @Test
        void shouldReturnPageWithExpectedSize() {
            // given
            int expectedSize = 3;

            UUID houseUuid = HOUSE_UUID_THIRD_IN_DB;

            // when
            Page<Person> actual = personRepository.findAllByHouseUuid(houseUuid, request);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
        }

        @Test
        void shouldReturnEmptyPage() {
            // given
            int expectedSize = 0;
            UUID houseUuid = INCORRECT_UUID;

            // when
            Page<Person> actual = personRepository.findAllByHouseUuid(houseUuid, request);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
        }
    }

    @Nested
    class FindByPersonHouseHistoriesHouseUuidAndPersonHouseHistoriesType {

        @Test
        void shouldReturnPageWithExpectedSize() {
            // given
            int expectedSize = 2;

            UUID houseUuid = HOUSE_UUID_FOURTH_IN_DB;

            TypePerson typePerson = TypePerson.TENANT;

            // when
            Page<Person> actual = personRepository.findByPersonHouseHistoriesHouseUuidAndPersonHouseHistoriesType(houseUuid, typePerson, request);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
        }

        @Test
        void shouldReturnEmptyPage() {
            // given
            int expectedSize = 0;
            UUID houseUuid = INCORRECT_UUID;
            TypePerson typePerson = TypePerson.TENANT;

            // when
            Page<Person> actual = personRepository.findByPersonHouseHistoriesHouseUuidAndPersonHouseHistoriesType(houseUuid, typePerson, request);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
        }
    }

    @Nested
    class FindPersonsFullTextSearch {

        @Test
        void shouldReturnPageWithExpectedSize() {
            // given
            int expectedSize = 3;
            String searchTerm = "на";

            // when
            Page<Person> actual = personRepository.findPersonsFullTextSearch(searchTerm, request);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
        }

        @Test
        void shouldReturnEmptyPage() {
            // given
            int expectedSize = 0;

            String searchTerm = "Кондрат";

            // when
            Page<Person> actual = personRepository.findPersonsFullTextSearch(searchTerm, request);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
        }
    }

    @Nested
    class DeletePersonByUuid {

        @Test
        void shouldDeleteExpectedValue() {
            // given
            UUID personUuid = PERSON_UUID_TENTH_IN_DB;

            // when
            personRepository.deletePersonByUuid(personUuid);

            Optional<Person> actual = personRepository.findPersonByUuid(personUuid);

            // then
            assertThat(actual).isEmpty();
        }
    }
}