package ru.clevertec.house.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.enums.TypePerson;
import util.PostgresSqlContainerInitializer;
import util.initdata.TestDataForHouse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static util.initdata.TestDataForHouse.HOUSE_UUID;
import static util.initdata.TestDataForHouse.HOUSE_UUID_SECOND_IN_DB;
import static util.initdata.TestDataForHouse.HOUSE_UUID_THIRD_IN_DB;
import static util.initdata.TestDataForHouse.INCORRECT_UUID;
import static util.initdata.TestDataForPerson.PERSON_UUID_SIXTH_IN_DB;

@RequiredArgsConstructor
class HouseRepositoryTest extends PostgresSqlContainerInitializer {

    private final HouseRepository houseRepository;

    final PageRequest request = PageRequest.of(0, 5);

    @Nested
    class FindByUuid {

        @Test
        void shouldReturnExpectedValue() {
            // given
            House expected = TestDataForHouse.getFirstHouseInDB();

            // when
            House actual = houseRepository.findHouseByUuid(expected.getUuid()).get();

            // then
            assertThat(actual.getId()).isEqualTo(expected.getId());
            assertThat(actual.getUuid()).isEqualTo(expected.getUuid());
            assertThat(actual.getArea()).isEqualTo(expected.getArea());
            assertThat(actual.getCountry()).isEqualTo(expected.getCountry());
            assertThat(actual.getStreet()).isEqualTo(expected.getStreet());
            assertThat(actual.getNumber()).isEqualTo(expected.getNumber());
            assertThat(actual.getCreateDate()).isEqualTo(expected.getCreateDate());
        }

        @Test
        void shouldReturnEmptyOptional() {
            // given
            UUID houseUuid = HOUSE_UUID;

            // when
            Optional<House> actual = houseRepository.findHouseByUuid(houseUuid);

            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class FindAllByUuidIn {

        @Test
        void shouldReturnListOfHouse() {
            // given
            int expectedSize = 2;

            List<UUID> uuids = List.of(HOUSE_UUID_SECOND_IN_DB,
                    HOUSE_UUID_THIRD_IN_DB);

            // when
            List<House> actual = houseRepository.findAllByUuidIn(uuids);

            // then
            assertThat(actual.size()).isEqualTo(expectedSize);
        }

        @Test
        void shouldReturnEmptyLIst() {
            // given
            List<UUID> uuids = List.of();

            // when
            List<House> actual = houseRepository.findAllByUuidIn(uuids);

            // then
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class FindByHouseHistoriesPersonUuidAndHouseHistoriesType {

        @Test
        void shouldReturnPageWithExpectedSize() {
            // given
            int expectedSize = 1;

            UUID personUuid = PERSON_UUID_SIXTH_IN_DB;

            TypePerson typePerson = TypePerson.TENANT;

            // when
            Page<House> actual = houseRepository.findByHouseHistoriesPersonUuidAndHouseHistoriesType(personUuid, typePerson, request);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
        }

        @Test
        void shouldReturnEmptyPage() {
            // given
            int expectedSize = 0;

            UUID personUuid = INCORRECT_UUID;

            TypePerson typePerson = TypePerson.TENANT;

            // when
            Page<House> actual = houseRepository.findByHouseHistoriesPersonUuidAndHouseHistoriesType(personUuid, typePerson, request);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
        }
    }

    @Nested
    class FindByOwnersUuid {

        @Test
        void shouldReturnPageWithExpectedSize() {
            // given
            int expectedSize = 1;

            UUID personUuid = PERSON_UUID_SIXTH_IN_DB;

            // when
            Page<House> actual = houseRepository.findByOwnersUuid(personUuid, request);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
        }

        @Test
        void shouldReturnEmptyPage() {
            // given
            int expectedSize = 0;

            UUID personUuid = INCORRECT_UUID;

            // when
            Page<House> actual = houseRepository.findByOwnersUuid(personUuid, request);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
        }
    }

    @Nested
    class FindHousesFullTextSearch {

        @Test
        void shouldReturnPageWithExpectedSize() {
            // given
            int expectedSize = 2;

            String searchTerm = "ви";

            // when
            Page<House> actual = houseRepository.findHousesFullTextSearch(searchTerm, request);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
        }

        @Test
        void shouldReturnEmptyPage() {
            // given
            int expectedSize = 0;

            String searchTerm = "Нева";

            // when
            Page<House> actual = houseRepository.findHousesFullTextSearch(searchTerm, request);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
        }
    }

    @Nested
    class DeleteHouseByUuid {

        @Test
        void shouldDeleteExpectedValue() {
            // given
            UUID houseUuid = HOUSE_UUID_SECOND_IN_DB;

            // when
            houseRepository.deleteHouseByUuid(houseUuid);

            Optional<House> actual = houseRepository.findHouseByUuid(houseUuid);

            // then
            assertThat(actual).isEmpty();
        }
    }
}