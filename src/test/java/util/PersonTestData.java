package util;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.house.dto.request.PersonRequest;
import ru.clevertec.house.dto.response.PersonResponse;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.HouseHistory;
import ru.clevertec.house.entity.Passport;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.enums.Sex;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static util.initdata.ConstantsForPerson.PERSON_CREATE_DATE;
import static util.initdata.ConstantsForPerson.PERSON_ID;
import static util.initdata.ConstantsForPerson.PERSON_NAME;
import static util.initdata.ConstantsForPerson.PERSON_SEX;
import static util.initdata.ConstantsForPerson.PERSON_SURNAME;
import static util.initdata.ConstantsForPerson.PERSON_UPDATE_DATE;
import static util.initdata.ConstantsForPerson.PERSON_UUID;

@Data
@Builder(setterPrefix = "with")
public class PersonTestData {

    @Builder.Default
    private Long id = PERSON_ID;

    @Builder.Default
    private UUID uuid = PERSON_UUID;

    @Builder.Default
    private String name = PERSON_NAME;

    @Builder.Default
    private String surname = PERSON_SURNAME;

    @Builder.Default
    private Sex sex = PERSON_SEX;

    @Builder.Default
    private Passport passport = PassportTestData.builder()
            .build()
            .getPassport();

    @Builder.Default
    private LocalDateTime createDate = PERSON_CREATE_DATE;

    @Builder.Default
    private LocalDateTime updateDate = PERSON_UPDATE_DATE;

    @Builder.Default
    private House house = HouseTestData.builder()
            .build()
            .getEntity();

    @Builder.Default
    private List<House> ownedHouses = List.of();

    @Builder.Default
    private Set<HouseHistory> personHouseHistories = Set.of();

    public Person getEntity() {
        return new Person(id, uuid, name, surname, sex, passport, createDate, updateDate, house, ownedHouses, personHouseHistories);
    }

    public PersonRequest getRequestDto() {
        return new PersonRequest(name, surname, sex, passport, house.getUuid(), ownedHouses);
    }

    public Optional<Person> getOptionalEntity() {
        return Optional.of(getEntity());
    }

    public PersonResponse getResponseDto() {
        return new PersonResponse(uuid, name, surname, sex, passport, house.getUuid(), createDate, updateDate);
    }

    public List<Person> getListOfPerson() {
        return List.of(PersonTestData.builder()
                        .build()
                        .getEntity(),
                PersonTestData.builder()
                        .withId(10L)
                        .withName("Леонид")
                        .withSurname("Боровик")
                        .withSex(Sex.FEMALE)
                        .withPassport(PassportTestData.builder()
                                .withSeries("IO")
                                .withNumber("139248")
                                .build()
                                .getPassport())
                        .withCreateDate(LocalDateTime.of(2020, 2, 1, 10, 0, 0))
                        .withUpdateDate(LocalDateTime.of(2022, 5, 2, 13, 10, 0))
                        .build()
                        .getEntity());
    }
}