package ru.clevertec.house.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.house.dto.request.PersonRequest;
import ru.clevertec.house.dto.response.PersonResponse;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Passport;
import ru.clevertec.house.enums.Sex;
import ru.clevertec.house.exception.NotFoundException;
import util.PostgresSqlContainerInitializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

@RequiredArgsConstructor
public class PersonServiceImplIT extends PostgresSqlContainerInitializer {

    private final PersonServiceImpl personService;

    @Test
    void shouldReturnPersonResponseWhenFindById() {
        // given
        UUID personUuid = UUID.fromString("922e0213-e543-48ef-b8cb-92592afd5100");

        // when
        PersonResponse response = personService.findById(personUuid);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUuid()).isEqualTo(personUuid);
    }

    @Test
    void shouldReturnPageWithListOfPersonResponseWhenFindAll() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 15);

        // when
        Page<PersonResponse> response = personService.findAll(pageRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty();
    }

    @Test
    void shouldReturnPageWithListOfPersonsWhichLiveInHouse() {
        // given
        UUID houseUuid = UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13");
        PageRequest pageRequest = PageRequest.of(0, 15);
        int expectedSize = 3;

        // when
        Page<PersonResponse> response = personService.findPersonsWhichLiveInHouse(houseUuid, pageRequest);

        // then
        assertThat(response.getTotalElements()).isEqualTo(expectedSize);
    }

    @Test
    void shouldReturnPageWithListOfPersonsWhichSomeTimeLiveInHouse() {
        // given
        UUID houseUuid = UUID.fromString("d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14");
        PageRequest pageRequest = PageRequest.of(0, 15);
        int expectedSize = 2;

        // when
        Page<PersonResponse> response = personService.findPersonsWhichSomeTimeLiveInHouse(houseUuid, pageRequest);

        // then
        assertThat(response.getTotalElements()).isEqualTo(expectedSize);
    }

    @Test
    void shouldReturnPageWithListOfPersonsWhichSomeTimeOwnHouse() {
        // given
        UUID houseUuid = UUID.fromString("e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15");
        PageRequest pageRequest = PageRequest.of(0, 15);
        int expectedSize = 3;

        // when
        Page<PersonResponse> response = personService.findPersonsWhichSomeTimeOwnHouse(houseUuid, pageRequest);

        // then
        assertThat(response.getTotalElements()).isEqualTo(expectedSize);
    }

    @Test
    void shouldReturnPageWithListOfPersonResponseFindWithFullTextSearch() {
        // given
        String searchTerm = "ре";
        PageRequest pageRequest = PageRequest.of(0, 15);
        int expectedSize = 1;

        // when
        Page<PersonResponse> response = personService.findPersonsFullTextSearch(searchTerm, pageRequest);

        // then
        assertThat(response.getTotalElements()).isEqualTo(expectedSize);
    }

    @Test
    void shouldReturnPersonResponseWhenSaveInDB() {
        // given
        PersonRequest personToSave = PersonRequest.builder()
                .name("Сергей")
                .surname("Васильков")
                .sex(Sex.MALE)
                .passport(Passport.builder()
                        .series("OP")
                        .number("486259")
                        .build())
                .houseUUID(UUID.fromString("9724b9b8-216d-4ab9-92eb-e6e06029580d"))
                .ownedHouses(List.of(House.builder()
                        .uuid(UUID.fromString("c8adac62-9266-486f-8540-21e976b635b3"))
                        .area("Гомельская")
                        .country("Республика Беларусь")
                        .city("Наровля")
                        .street("Лесная")
                        .number(25)
                        .build()))
                .build();

        // when
        PersonResponse savedPerson = personService.save(personToSave);

        // then
        assertThat(savedPerson.getUuid()).isNotNull();
        assertThat(savedPerson.getName()).isEqualTo(personToSave.getName());
        assertThat(savedPerson.getSurname()).isEqualTo(personToSave.getSurname());
        assertThat(savedPerson.getSex()).isEqualTo(personToSave.getSex());
        assertThat(savedPerson.getPassport()).isEqualTo(personToSave.getPassport());
        assertThat(savedPerson.getHouseUUID()).isEqualTo(personToSave.getHouseUUID());
    }

    @Test
    void shouldReturnUpdatedPersonResponseWhenUpdateInDB() {
        // given
        UUID personUuid = UUID.fromString("63a1faca-a963-4d4b-bfb9-2dafaedc36fe");
        PersonRequest personToUpdate = PersonRequest.builder()
                .name("Наташа")
                .surname("Жарова")
                .sex(Sex.FEMALE)
                .passport(Passport.builder()
                        .series("XC")
                        .number("433259")
                        .build())
                .houseUUID(UUID.fromString("9724b9b8-216d-4ab9-92eb-e6e06029580d"))
                .ownedHouses(List.of(House.builder()
                        .uuid(UUID.fromString("0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6"))
                        .area("Гомельская")
                        .country("Беларусь")
                        .city("Ельск")
                        .street("Ленина")
                        .number(2)
                        .build()))
                .build();

        // when
        PersonResponse updatedPerson = personService.update(personUuid, personToUpdate);

        // then
        assertThat(updatedPerson.getUuid()).isEqualTo(personUuid);
        assertThat(updatedPerson.getName()).isEqualTo(personToUpdate.getName());
        assertThat(updatedPerson.getSurname()).isEqualTo(personToUpdate.getSurname());
        assertThat(updatedPerson.getSex()).isEqualTo(personToUpdate.getSex());
        assertThat(updatedPerson.getPassport()).isEqualTo(personToUpdate.getPassport());
        assertThat(updatedPerson.getHouseUUID()).isEqualTo(personToUpdate.getHouseUUID());
    }

    @Test
    void shouldReturnUpdatedPersonResponseWhenPatchUpdateInDB() {
        // given
        UUID personUuid = UUID.fromString("40291d40-5948-448c-b66a-d09591d3500f");
        String name = "Евгений";
        String surname = "Никитин";
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", name);
        fields.put("surname", surname);

        // when
        PersonResponse patchUpdatedPerson = personService.patchUpdate(personUuid, fields);

        // then
        assertThat(patchUpdatedPerson.getUuid()).isEqualTo(personUuid);
        assertThat(patchUpdatedPerson.getName()).isEqualTo(name);
        assertThat(patchUpdatedPerson.getSurname()).isEqualTo(surname);
    }

    @Test
    void shouldDeletePersonInDB() {
        // given
        UUID personUuid = UUID.fromString("3df38f0a-09bb-4bbc-a80c-2f827b6f9d75");

        // when
        personService.delete(personUuid);

        // then
        assertThrows(NotFoundException.class, () -> personService.findById(personUuid));
    }
}
