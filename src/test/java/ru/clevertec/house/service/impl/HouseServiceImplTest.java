package ru.clevertec.house.service.impl;

import by.clevertec.exception.CheckEmptyException;
import by.clevertec.exception.HouseNotEmptyException;
import by.clevertec.exception.NotFoundException;
import config.ServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.house.dto.request.HouseRequest;
import ru.clevertec.house.dto.response.HouseResponse;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.enums.TypePerson;
import ru.clevertec.house.mapper.HouseMapper;
import ru.clevertec.house.repository.HouseRepository;
import util.HouseTestData;
import util.PersonTestData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static util.initdata.TestDataForHouse.HOUSE_UUID;
import static util.initdata.TestDataForHouse.INCORRECT_UUID;
import static util.initdata.TestDataForHouse.UPDATE_HOUSE_AREA;
import static util.initdata.TestDataForHouse.UPDATE_HOUSE_CITY;
import static util.initdata.TestDataForPerson.DEFAULT_PAGE_REQUEST_FOR_IT;
import static util.initdata.TestDataForPerson.PERSON_UUID;

@ServiceTest
@RequiredArgsConstructor
class HouseServiceImplTest {

    private HouseServiceImpl houseService;

    @Mock
    private HouseRepository houseRepository;

    private final HouseMapper houseMapper;

    @Captor
    private ArgumentCaptor<House> captor;

    @BeforeEach
    void setUp() {
        houseService = new HouseServiceImpl(houseRepository);
    }

    @Nested
    class FindById {

        @Test
        void shouldReturnExpectedHouseByUUID() {
            // given
            UUID houseUuid = HOUSE_UUID;

            Optional<House> houseInDB = HouseTestData.builder()
                    .build()
                    .getOptionalEntity();

            HouseResponse expected = houseMapper.toResponse(houseInDB.get());

            when(houseRepository.findHouseByUuid(houseUuid))
                    .thenReturn(houseInDB);

            // when
            HouseResponse actual = houseService.findById(houseUuid);

            // then
            assertThat(actual).isEqualTo(expected);
            verify(houseRepository, times(1)).findHouseByUuid(houseUuid);
        }

        @Test
        void shouldReturnThrowIfHouseNotExistWithUUID() {
            // given
            UUID incorrectUuid = INCORRECT_UUID;

            Optional<House> houseInDB = Optional.empty();

            when(houseRepository.findHouseByUuid(incorrectUuid))
                    .thenReturn(houseInDB);

            // when, then
            assertThrows(NotFoundException.class, () -> houseService.findById(incorrectUuid));
            verify(houseRepository, times(1)).findHouseByUuid(incorrectUuid);
        }
    }

    @Nested
    class FindHouses {

        @Test
        void shouldReturnListOfHouseResponse() {
            // given
            int expectedSize = 1;
            List<House> houseList = List.of(HouseTestData.builder()
                    .build()
                    .getEntity());
            Page<House> page = new PageImpl<>(houseList);

            when(houseRepository.findAll(any(PageRequest.class)))
                    .thenReturn(page);

            // when
            Page<HouseResponse> actual = houseService.findAll(DEFAULT_PAGE_REQUEST_FOR_IT);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
            verify(houseRepository, times(1)).findAll(any(PageRequest.class));
        }

        @Test
        void shouldCheckEmpty() {
            // given
            Page<House> page = new PageImpl<>(List.of());

            when(houseRepository.findAll(any(PageRequest.class)))
                    .thenReturn(page);

            // when
            Page<HouseResponse> actual = houseService.findAll(DEFAULT_PAGE_REQUEST_FOR_IT);

            // then
            assertThat(actual).isEmpty();
            verify(houseRepository, times(1)).findAll(any(PageRequest.class));
        }

        @Test
        void shouldReturnListOfHousesWhichSomeTimeLivesPerson() {
            // given
            int expectedSize = 1;
            UUID personUuid = PERSON_UUID;
            TypePerson typePerson = TypePerson.TENANT;
            PageRequest pageRequest = DEFAULT_PAGE_REQUEST_FOR_IT;

            List<House> houseList = List.of(HouseTestData.builder()
                    .build()
                    .getEntity());

            Page<House> page = new PageImpl<>(houseList);

            when(houseRepository.findByHouseHistoriesPersonUuidAndHouseHistoriesType(personUuid, typePerson, pageRequest))
                    .thenReturn(page);

            // when
            Page<HouseResponse> actual = houseService.findHousesWhichSomeTimeLivesPerson(personUuid, pageRequest);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
            verify(houseRepository, times(1))
                    .findByHouseHistoriesPersonUuidAndHouseHistoriesType(personUuid, typePerson, pageRequest);
        }

        @Test
        void shouldReturnListOfHousesWhichOwnPerson() {
            // given
            int expectedSize = 1;
            UUID personUuid = PERSON_UUID;
            PageRequest pageRequest = DEFAULT_PAGE_REQUEST_FOR_IT;

            List<House> houseList = List.of(HouseTestData.builder()
                    .build()
                    .getEntity());

            Page<House> page = new PageImpl<>(houseList);

            when(houseRepository.findByOwnersUuid(personUuid, pageRequest))
                    .thenReturn(page);

            // when
            Page<HouseResponse> actual = houseService.findHousesWhichOwnPerson(personUuid, pageRequest);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
            verify(houseRepository, times(1)).findByOwnersUuid(personUuid, pageRequest);
        }

        @Test
        void shouldReturnListOfHousesWhichSomeTimeOwnPerson() {
            // given
            int expectedSize = 1;
            UUID personUuid = PERSON_UUID;
            TypePerson typePerson = TypePerson.OWNER;
            PageRequest pageRequest = DEFAULT_PAGE_REQUEST_FOR_IT;

            List<House> houseList = List.of(HouseTestData.builder()
                    .build()
                    .getEntity());

            Page<House> page = new PageImpl<>(houseList);

            when(houseRepository.findByHouseHistoriesPersonUuidAndHouseHistoriesType(personUuid, typePerson, pageRequest))
                    .thenReturn(page);

            // when
            Page<HouseResponse> actual = houseService.findHousesWhichSomeTimeOwnPerson(personUuid, pageRequest);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
            verify(houseRepository, times(1))
                    .findByHouseHistoriesPersonUuidAndHouseHistoriesType(personUuid, typePerson, pageRequest);
        }

        @Test
        void shouldReturnListOfHousesFindWithFullTextSearch() {
            // given
            int expectedSize = 1;
            String searchTerm = "ин";
            PageRequest pageRequest = DEFAULT_PAGE_REQUEST_FOR_IT;

            List<House> houseList = List.of(HouseTestData.builder()
                    .build()
                    .getEntity());

            Page<House> page = new PageImpl<>(houseList);

            when(houseRepository.findHousesFullTextSearch(searchTerm, pageRequest))
                    .thenReturn(page);

            // when
            Page<HouseResponse> actual = houseService.findHousesFullTextSearch(searchTerm, pageRequest);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
            verify(houseRepository, times(1)).findHousesFullTextSearch(searchTerm, pageRequest);
        }
    }

    @Nested
    class Save {

        @Test
        void shouldReturnSavedHouseResponseIfValidHouseRequest() {
            // given
            HouseRequest houseRequest = HouseTestData.builder()
                    .build()
                    .getRequestDto();

            House expected = houseMapper.toHouse(houseRequest);

            House savedHouse = HouseTestData.builder()
                    .build()
                    .getEntity();

            when(houseRepository.save(any(House.class)))
                    .thenReturn(savedHouse);

            // when
            houseService.save(houseRequest);

            // then
            verify(houseRepository, times(1)).save(captor.capture());
            House actual = captor.getValue();
            assertThat(actual.getId()).isEqualTo(expected.getId());
            assertThat(actual.getCity()).isEqualTo(expected.getCity());
        }
    }

    @Nested
    class Update {

        @Test
        void shouldReturnUpdatedHouseResponseIfValidHouseRequest() {
            // given
            UUID houseUuid = HOUSE_UUID;

            HouseRequest requestDto = HouseTestData.builder()
                    .withArea(UPDATE_HOUSE_AREA)
                    .withCity(UPDATE_HOUSE_CITY)
                    .build()
                    .getRequestDto();

            House houseToUpdate = houseMapper.toHouse(requestDto);

            Optional<House> optionalEntity = HouseTestData.builder()
                    .build()
                    .getOptionalEntity();

            House updatedHouse = HouseTestData.builder()
                    .withArea(UPDATE_HOUSE_AREA)
                    .withCity(UPDATE_HOUSE_CITY)
                    .build()
                    .getEntity();

            HouseResponse expected = HouseTestData.builder()
                    .withArea(UPDATE_HOUSE_AREA)
                    .withCity(UPDATE_HOUSE_CITY)
                    .build()
                    .getResponseDto();

            when(houseRepository.findHouseByUuid(houseUuid))
                    .thenReturn(optionalEntity);

            houseToUpdate.setId(optionalEntity.get().getId());
            houseToUpdate.setUuid(optionalEntity.get().getUuid());
            houseToUpdate.setCreateDate(optionalEntity.get().getCreateDate());

            when(houseRepository.save(any(House.class)))
                    .thenReturn(updatedHouse);

            // when
            HouseResponse actual = houseService.update(houseUuid, requestDto);

            // then
            assertThat(actual).isEqualTo(expected);
            verify(houseRepository, times(1)).findHouseByUuid(houseUuid);
        }

        @Test
        void shouldReturnUpdatedPatchHouseResponseIfValidFields() {
            // given
            UUID houseUuid = HOUSE_UUID;

            Map<String, Object> fieldsToUpdate = new HashMap<>();
            fieldsToUpdate.put("area", UPDATE_HOUSE_AREA);
            fieldsToUpdate.put("city", UPDATE_HOUSE_CITY);

            Optional<House> optionalEntity = HouseTestData.builder()
                    .build()
                    .getOptionalEntity();

            House patchedHouse = HouseTestData.builder()
                    .withArea(UPDATE_HOUSE_AREA)
                    .withCity(UPDATE_HOUSE_CITY)
                    .build()
                    .getEntity();

            HouseResponse expected = HouseTestData.builder()
                    .withArea(UPDATE_HOUSE_AREA)
                    .withCity(UPDATE_HOUSE_CITY)
                    .build()
                    .getResponseDto();

            when(houseRepository.findHouseByUuid(houseUuid))
                    .thenReturn(optionalEntity);

            when(houseRepository.save(any(House.class)))
                    .thenReturn(patchedHouse);

            // when
            HouseResponse actual = houseService.patchUpdate(houseUuid, fieldsToUpdate);

            // then
            assertThat(actual).isEqualTo(expected);
            verify(houseRepository, times(1)).findHouseByUuid(houseUuid);
        }

        @Test
        void shouldReturnThrowIfHouseNotExistWithUUIDWhenUpdate() {
            // given
            UUID incorrectUUID = INCORRECT_UUID;
            HouseRequest requestDto = HouseTestData.builder()
                    .withArea(UPDATE_HOUSE_AREA)
                    .withCity(UPDATE_HOUSE_CITY)
                    .build()
                    .getRequestDto();

            when(houseRepository.findHouseByUuid(incorrectUUID))
                    .thenReturn(Optional.empty());

            // when, then
            assertThrows(NotFoundException.class, () -> houseService.update(incorrectUUID, requestDto));
            verify(houseRepository, times(1)).findHouseByUuid(incorrectUUID);
            verify(houseRepository, never()).save(any(House.class));
        }

        @Test
        void shouldReturnThrowWhenFieldsAreEmpty() {
            // given
            UUID houseUuid = HOUSE_UUID;
            Map<String, Object> fields = new HashMap<>();

            // when
            Throwable thrown = catchThrowable(() -> houseService.patchUpdate(houseUuid, fields));

            // then
            assertThat(thrown).isInstanceOf(CheckEmptyException.class);
        }

        @Test
        void shouldReturnThrowIfHouseNotExistWithUUIDWhenPatchUpdate() {
            // given
            UUID houseUuid = HOUSE_UUID;
            String incorrectData = "incorrect";
            Map<String, Object> fields = new HashMap<>();
            fields.put(incorrectData, incorrectData);

            when(houseRepository.findHouseByUuid(houseUuid)).thenReturn(Optional.empty());

            // when
            Throwable thrown = catchThrowable(() -> houseService.patchUpdate(houseUuid, fields));

            // then
            assertThat(thrown).isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    class Delete {

        @Test
        void shouldDeleteHouseByUUID() {
            // given
            UUID houseUuid = HOUSE_UUID;

            House houseInDB = HouseTestData.builder()
                    .build()
                    .getEntity();
            houseInDB.setResidents(new HashSet<>());

            when(houseRepository.findHouseByUuid(houseUuid))
                    .thenReturn(Optional.of(houseInDB));

            // when
            houseService.delete(houseUuid);

            // then
            verify(houseRepository, times(1)).findHouseByUuid(houseUuid);
            verify(houseRepository, times(1)).deleteHouseByUuid(houseUuid);
        }

        @Test
        void shouldReturnThrowIfHouseNotExistWithUUID() {
            // given
            UUID incorrectUUID = INCORRECT_UUID;

            // when, then
            assertThrows(NotFoundException.class, () -> houseService.delete(incorrectUUID));
            verify(houseRepository, never()).deleteHouseByUuid(incorrectUUID);
        }

        @Test
        void shouldReturnThrowIfHouseHasResidents() {
            // given
            UUID houseUuid = HOUSE_UUID;

            House house = HouseTestData.builder()
                    .build()
                    .getEntity();

            Person person = PersonTestData.builder()
                    .build()
                    .getEntity();
            house.setResidents(Set.of(person));
            when(houseRepository.findHouseByUuid(houseUuid)).thenReturn(Optional.of(house));

            // when
            Throwable thrown = catchThrowable(() -> houseService.delete(houseUuid));

            // then
            assertThat(thrown).isInstanceOf(HouseNotEmptyException.class);
        }
    }
}