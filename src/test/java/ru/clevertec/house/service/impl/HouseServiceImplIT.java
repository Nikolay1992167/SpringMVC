package ru.clevertec.house.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.house.dto.request.HouseRequest;
import ru.clevertec.house.dto.response.HouseResponse;
import ru.clevertec.house.exception.NotFoundException;
import util.PostgresqlTestContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

@RequiredArgsConstructor
public class HouseServiceImplIT extends PostgresqlTestContainer {

    private final HouseServiceImpl houseService;

    @Test
    void shouldReturnHouseResponseWhenFindById() {
        // given
        UUID houseUuid = UUID.fromString("0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6");

        // when
        HouseResponse response = houseService.findById(houseUuid);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUuid()).isEqualTo(houseUuid);
    }

    @Test
    void shouldReturnPageWithListOfHouseResponseWhenFindAll() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 15);

        // when
        Page<HouseResponse> response = houseService.findAll(pageRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty();
    }

    @Test
    void shouldReturnPageWithListOfHouseResponseWhichSomeTimeLivesPerson() {
        // given
        UUID personUuid = UUID.fromString("9aa78d35-fb66-45a6-8570-f81513ef8272");
        PageRequest pageRequest = PageRequest.of(0, 15);
        int expectedSize = 1;

        // when
        Page<HouseResponse> response = houseService.findHousesWhichSomeTimeLivesPerson(personUuid, pageRequest);

        // then
        assertThat(response.getTotalElements()).isEqualTo(expectedSize);
    }

    @Test
    void shouldReturnPageWithListOfHouseResponseWhichOwnPerson() {
        // given
        UUID personUuid = UUID.fromString("9aa78d35-fb66-45a6-8570-f81513ef8272");
        PageRequest pageRequest = PageRequest.of(0, 15);
        int expectedSize = 1;

        // when
        Page<HouseResponse> response = houseService.findHousesWhichOwnPerson(personUuid, pageRequest);

        // then
        assertThat(response.getTotalElements()).isEqualTo(expectedSize);
    }

    @Test
    void shouldReturnPageWithListOfHouseResponseWhichSomeTimeOwnPerson() {
        // given
        UUID personUuid = UUID.fromString("24277f25-81ee-4925-885c-a639d0211dde");
        PageRequest pageRequest = PageRequest.of(0, 15);
        int expectedSize = 1;

        // when
        Page<HouseResponse> response = houseService.findHousesWhichSomeTimeOwnPerson(personUuid, pageRequest);

        // then
        assertThat(response.getTotalElements()).isEqualTo(expectedSize);
    }

    @Test
    void shouldReturnPageWithListOfHouseResponseFindWithFullTextSearch() {
        // given
        String searchTerm = "ин";
        PageRequest pageRequest = PageRequest.of(0, 15);
        int expectedSize = 4;

        // when
        Page<HouseResponse> response = houseService.findHousesFullTextSearch(searchTerm, pageRequest);

        // then
        assertThat(response.getTotalElements()).isEqualTo(expectedSize);
    }

    @Test
    void shouldReturnHouseResponseWhenSaveInDB() {
        // given
        HouseRequest houseToSave = HouseRequest.builder()
                .area("Гомельская")
                .country("Республика Беларусь")
                .city("Логойск")
                .street("Лесная")
                .number(8)
                .build();

        // when
        HouseResponse savedHouse = houseService.save(houseToSave);

        // then
        assertThat(savedHouse.getUuid()).isNotNull();
        assertThat(savedHouse.getArea()).isEqualTo(houseToSave.getArea());
        assertThat(savedHouse.getCountry()).isEqualTo(houseToSave.getCountry());
        assertThat(savedHouse.getCity()).isEqualTo(houseToSave.getCity());
        assertThat(savedHouse.getStreet()).isEqualTo(houseToSave.getStreet());
        assertThat(savedHouse.getNumber()).isEqualTo(houseToSave.getNumber());
    }

    @Test
    void shouldReturnUpdatedHouseResponseWhenUpdateInDB() {
        // given
        UUID houseUuid = UUID.fromString("d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14");
        HouseRequest houseToUpdate = HouseRequest.builder()
                .area("Витебская")
                .country("Республика Беларусь")
                .city("Полоцк")
                .street("Промышленная")
                .number(5)
                .build();

        // when
        HouseResponse updatedHouse = houseService.update(houseUuid, houseToUpdate);

        // then
        assertThat(updatedHouse.getUuid()).isEqualTo(houseUuid);
        assertThat(updatedHouse.getArea()).isEqualTo(houseToUpdate.getArea());
        assertThat(updatedHouse.getCountry()).isEqualTo(houseToUpdate.getCountry());
        assertThat(updatedHouse.getCity()).isEqualTo(houseToUpdate.getCity());
        assertThat(updatedHouse.getStreet()).isEqualTo(houseToUpdate.getStreet());
        assertThat(updatedHouse.getNumber()).isEqualTo(houseToUpdate.getNumber());
    }

    @Test
    void shouldReturnUpdatedHouseResponseWhenPatchUpdateInDB() {
        // given
        UUID houseUuid = UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13");
        String area = "Витебская";
        int number = 12;
        Map<String, Object> fields = new HashMap<>();
        fields.put("area", area);
        fields.put("number", number);

        // when
        HouseResponse patchUpdatedHouse = houseService.patchUpdate(houseUuid, fields);

        // then
        assertThat(patchUpdatedHouse.getUuid()).isEqualTo(houseUuid);
        assertThat(patchUpdatedHouse.getArea()).isEqualTo(area);
        assertThat(patchUpdatedHouse.getNumber()).isEqualTo(number);
    }

    @Test
    void shouldDeleteHouseInDB() {
        // given
        UUID houseUuid = UUID.fromString("9724b9b8-216d-4ab9-92eb-e6e06029580d");

        // when
        houseService.delete(houseUuid);

        // then
        assertThrows(NotFoundException.class, () -> houseService.findById(houseUuid));

    }
}
