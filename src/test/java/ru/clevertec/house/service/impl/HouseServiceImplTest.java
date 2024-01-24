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

import ru.clevertec.house.dto.request.HouseRequest;
import ru.clevertec.house.dto.response.HouseResponse;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.mapper.HouseMapper;
import util.HouseTestData;

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
import static util.initdata.ConstantsForHouse.UPDATE_HOUSE_AREA;
import static util.initdata.ConstantsForHouse.UPDATE_HOUSE_CITY;

@ExtendWith(MockitoExtension.class)
class HouseServiceImplTest {

    @InjectMocks
    private HouseServiceImpl houseService;

    @Mock
    private HouseDao houseDao;

    private final HouseMapper houseMapper = Mappers.getMapper(HouseMapper.class);

    @Captor
    private ArgumentCaptor<House> captor;

    @Nested
    class FindById {

        @Test
        void shouldReturnExpectedHouseByUUID() {
            // given
            UUID houseUuid = HOUSE_UUID;

            Optional<House> houseInDB = HouseTestData.builder()
                    .build()
                    .getOptionalEntity();

            HouseResponse expected = HouseTestData.builder()
                    .build()
                    .getResponseDto();

            when(houseDao.findById(houseUuid))
                    .thenReturn(houseInDB);

            // when
            HouseResponse actual = houseService.findById(houseUuid);

            // then
            assertThat(actual).isEqualTo(expected);
            verify(houseDao, times(1)).findById(houseUuid);
        }

        @Test
        void shouldReturnThrowIfHouseNotExistWithUUID() {
            // given
            UUID incorrectUuid = INCORRECT_UUID;

            Optional<House> houseInDB = Optional.empty();

            when(houseDao.findById(incorrectUuid))
                    .thenReturn(houseInDB);

            // when, then
            assertThrows(NotFoundException.class, () -> houseService.findById(incorrectUuid));
            verify(houseDao, times(1)).findById(incorrectUuid);
        }
    }

    @Nested
    class FindAll {

        @Test
        void shouldReturnListOfHouseResponse() {
            // given
            int expectedSize = 1;
            int pageNumber = 1;
            int pageSize = 10;

            List<House> houseList = List.of(HouseTestData.builder()
                    .build()
                    .getEntity());

            when(houseDao.findAll(pageNumber, pageSize))
                    .thenReturn(houseList);

            // when
            List<HouseResponse> actual = houseService.findAll(pageNumber, pageSize);

            // then
            assertThat(actual.size()).isEqualTo(expectedSize);
        }

        @Test
        void shouldCheckEmpty() {
            // given
            int pageNumber = 1;
            int pageSize = 10;

            when(houseDao.findAll(pageNumber, pageSize))
                    .thenReturn(List.of());

            // when
            List<HouseResponse> actual = houseService.findAll(pageNumber, pageSize);

            // then
            assertThat(actual).isEmpty();
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

            when(houseDao.save(expected))
                    .thenReturn(savedHouse);

            // when
            houseService.save(houseRequest);

            // then
            verify(houseDao, times(1)).save(captor.capture());
            House actual = captor.getValue();
            assertThat(actual).isEqualTo(expected);
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

            when(houseDao.findById(houseUuid))
                    .thenReturn(optionalEntity);

            houseToUpdate.setId(optionalEntity.get().getId());
            houseToUpdate.setUuid(optionalEntity.get().getUuid());
            houseToUpdate.setCreateDate(optionalEntity.get().getCreateDate());

            when(houseDao.update(houseToUpdate))
                    .thenReturn(updatedHouse);

            // when
            HouseResponse actual = houseService.update(houseUuid, requestDto);

            // then
            assertThat(actual).isEqualTo(expected);
            verify(houseDao, times(1)).findById(houseUuid);
        }

        @Test
        void shouldReturnThrowIfHouseNotExistWithUUID() {
            // given
            UUID incorrectUUID = INCORRECT_UUID;
            HouseRequest requestDto = HouseTestData.builder()
                    .withArea(UPDATE_HOUSE_AREA)
                    .withCity(UPDATE_HOUSE_CITY)
                    .build()
                    .getRequestDto();

            when(houseDao.findById(incorrectUUID))
                    .thenReturn(Optional.empty());

            // when, then
            assertThrows(NotFoundException.class, () -> houseService.update(incorrectUUID, requestDto));
            verify(houseDao, times(1)).findById(incorrectUUID);
            verify(houseDao, never()).update(any(House.class));
        }
    }

    @Nested
    class Delete {

        @Test
        void shouldDeleteHouseByUUID() {
            // given
            UUID houseUuid = HOUSE_UUID;

            Optional<House> optionalEntity = HouseTestData.builder()
                    .build()
                    .getOptionalEntity();

            when(houseDao.delete(houseUuid))
                    .thenReturn(optionalEntity);

            // when
            houseService.delete(houseUuid);

            // then
            verify(houseDao, times(1))
                    .delete(houseUuid);
        }

        @Test
        void shouldReturnThrowIfHouseNotExistWithUUID() {
            // given
            UUID incorrectUUID = INCORRECT_UUID;

            // when, then
            assertThrows(NotFoundException.class, () -> houseService.delete(incorrectUUID));
            verify(houseDao, times(1)).delete(incorrectUUID);
        }
    }
}