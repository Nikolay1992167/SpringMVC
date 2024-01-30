package util.initdata;

import org.springframework.data.domain.PageRequest;
import ru.clevertec.house.dto.request.PersonRequest;
import ru.clevertec.house.entity.Passport;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.enums.Sex;
import util.PersonTestData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TestDataForPerson {

    public static final Long PERSON_ID = 2L;
    public static final UUID PERSON_UUID = UUID.fromString("5633c57e-703c-4199-b340-18421d88f330");
    public static final String PERSON_NAME = "Геннадий";
    public static final String PERSON_SURNAME = "Воронко";
    public static final Sex PERSON_SEX = Sex.MALE;
    public static final LocalDateTime PERSON_CREATE_DATE = LocalDateTime.of(2023, 1, 1, 19, 0, 0);
    public static final LocalDateTime PERSON_UPDATE_DATE = LocalDateTime.of(2023, 1, 13, 19, 30, 0);
    public static final String UPDATE_PERSON_NAME = "Виктор";
    public static final String UPDATE_PERSON_SURNAME = "Строганов";
    public static final PageRequest DEFAULT_PAGE_REQUEST_FOR_IT = PageRequest.of(0, 15);
    public static final UUID PERSON_UUID_FIRST_IN_DB = UUID.fromString("9aa78d35-fb66-45a6-8570-f81513ef8272");
    public static final UUID PERSON_UUID_SECOND_IN_DB = UUID.fromString("922e0213-e543-48ef-b8cb-92592afd5100");
    public static final UUID PERSON_UUID_FOURTH_IN_DB = UUID.fromString("24277f25-81ee-4925-885c-a639d0211dde");
    public static final UUID PERSON_UUID_SIXTH_IN_DB = UUID.fromString("863db796-cf16-4c67-ad24-710d0d2f0341");
    public static final UUID PERSON_UUID_EIGHTH_IN_DB = UUID.fromString("3df38f0a-09bb-4bbc-a80c-2f827b6f9d75");
    public static final UUID PERSON_UUID_NINTH_IN_DB = UUID.fromString("63a1faca-a963-4d4b-bfb9-2dafaedc36fe");
    public static final UUID PERSON_UUID_TENTH_IN_DB = UUID.fromString("40291d40-5948-448c-b66a-d09591d3500f");

    public static PersonRequest getNewPersonForCreate() {
        return PersonTestData.builder()
                .withName("Сергей")
                .withSurname("Васильков")
                .withSex(Sex.MALE)
                .withPassport(Passport.builder()
                        .series("OP")
                        .number("486259")
                        .build())
                .withHouse(TestDataForHouse.getSecondHouseInDB())
                .withOwnedHouses(List.of(TestDataForHouse.getNewHouseForCreateWhichOwnPerson()))
                .build()
                .getRequestDto();
    }

    public static PersonRequest getPersonForUpdate() {
        return PersonTestData.builder()
                .withName("Наташа")
                .withSurname("Жарова")
                .withSex(Sex.FEMALE)
                .withPassport(Passport.builder()
                        .series("XC")
                        .number("433259")
                        .build())
                .withHouse(TestDataForHouse.getSecondHouseInDB())
                .withOwnedHouses(List.of(TestDataForHouse.getFirstHouseInDB()))
                .build()
                .getRequestDto();
    }

    public static Person getFirstPersonInDB() {
        return PersonTestData.builder()
                .withId(1L)
                .withUuid(UUID.fromString("9aa78d35-fb66-45a6-8570-f81513ef8272"))
                .withName("Марина")
                .withSurname("Громкая")
                .withSex(Sex.FEMALE)
                .withPassport(new Passport("HB", "123456"))
                .withCreateDate(LocalDateTime.of(2022, 3, 6, 10, 0, 0))
                .withUpdateDate(LocalDateTime.of(2022, 3, 6, 10, 0, 0))
                .withHouse(TestDataForHouse.getFirstHouseInDB())
                .build()
                .getEntity();
    }
}