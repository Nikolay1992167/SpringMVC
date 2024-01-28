package ru.clevertec.house.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Passport;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.enums.Sex;
import ru.clevertec.house.enums.TypePerson;
import util.PostgresSqlContainerInitializer;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static util.initdata.ConstantsForPerson.PERSON_UUID;

@RequiredArgsConstructor
class PersonRepositoryTest extends PostgresSqlContainerInitializer {

    private final PersonRepository personRepository;

    final PageRequest request = PageRequest.of(0, 5);

    @Nested
    class FindByUuid {

        @Test
        void shouldReturnExpectedValue() {
            // given
            Person expected = Person.builder()
                    .id(1L)
                    .uuid(UUID.fromString("9aa78d35-fb66-45a6-8570-f81513ef8272"))
                    .name("Марина")
                    .surname("Громкая")
                    .sex(Sex.FEMALE)
                    .passport(new Passport("HB", "123456"))
                    .createDate(LocalDateTime.of(2022, 3, 6, 10, 0, 0))
                    .updateDate(LocalDateTime.of(2022, 3, 6, 10, 0, 0))
                    .house(House.builder()
                            .id(1L)
                            .uuid(UUID.fromString("0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6"))
                            .area("Гомельская")
                            .country("Беларусь")
                            .city("Ельск")
                            .street("Ленина")
                            .number(2)
                            .createDate(LocalDateTime.of(2023, 12, 30, 12, 0, 0))
                            .build())
                    .build();

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
            UUID houseUuid = UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13");

            // when
            Page<Person> actual = personRepository.findAllByHouseUuid(houseUuid, request);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
        }

        @Test
        void shouldReturnEmptyPage() {
            // given
            int expectedSize = 0;
            UUID houseUuid = UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a99");

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
            UUID houseUuid = UUID.fromString("d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14");
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
            UUID houseUuid = UUID.fromString("d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a55");
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
            UUID personUuid = UUID.fromString("40291d40-5948-448c-b66a-d09591d3500f");

            // when
            personRepository.deletePersonByUuid(personUuid);
            Optional<Person> actual = personRepository.findPersonByUuid(personUuid);

            // then
            assertThat(actual).isEmpty();
        }
    }
}