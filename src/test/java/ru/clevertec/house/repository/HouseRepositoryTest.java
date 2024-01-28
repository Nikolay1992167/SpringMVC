package ru.clevertec.house.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.enums.TypePerson;
import util.PostgresSqlContainerInitializer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static util.initdata.ConstantsForHouse.HOUSE_UUID;

@RequiredArgsConstructor
class HouseRepositoryTest extends PostgresSqlContainerInitializer {

    private final HouseRepository houseRepository;

    final PageRequest request = PageRequest.of(0, 5);

    @Nested
    class FindByUuid {

        @Test
        void shouldReturnExpectedValue() {
            // given
            House expected = House.builder()
                    .id(1L)
                    .uuid(UUID.fromString("0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6"))
                    .area("Гомельская")
                    .country("Беларусь")
                    .city("Ельск")
                    .street("Ленина")
                    .number(2)
                    .createDate(LocalDateTime.of(2023, 12, 30, 12, 0, 0))
                    .build();

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
            List<UUID> uuids = List.of(UUID.fromString("9724b9b8-216d-4ab9-92eb-e6e06029580d"),
                    UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13"));

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
            UUID personUuid = UUID.fromString("863db796-cf16-4c67-ad24-710d0d2f0341");
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
            UUID personUuid = UUID.fromString("863db796-cf16-4c67-ad24-710d0d2f0342");
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
            UUID personUuid = UUID.fromString("863db796-cf16-4c67-ad24-710d0d2f0341");

            // when
            Page<House> actual = houseRepository.findByOwnersUuid(personUuid, request);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
        }

        @Test
        void shouldReturnEmptyPage() {
            // given
            int expectedSize = 0;
            UUID personUuid = UUID.fromString("863db796-cf16-4c67-ad24-710d0d2f0342");

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
            UUID houseUuid = UUID.fromString("9724b9b8-216d-4ab9-92eb-e6e06029580d");

            // when
            houseRepository.deleteHouseByUuid(houseUuid);
            Optional<House> actual = houseRepository.findHouseByUuid(houseUuid);

            // then
            assertThat(actual).isEmpty();
        }
    }
}