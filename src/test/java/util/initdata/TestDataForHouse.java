package util.initdata;

import ru.clevertec.house.dto.request.HouseRequest;
import ru.clevertec.house.entity.House;
import util.HouseTestData;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestDataForHouse {

    public static final Long HOUSE_ID = 4L;
    public static final UUID HOUSE_UUID = UUID.fromString("252d2a95-bf07-4436-aeec-8e3639a7f647");
    public static final UUID HOUSE_UUID_FOR_FIND = UUID.fromString("51fdc29c-9dca-41f9-a0be-3ab2625a9ab5");
    public static final String HOUSE_AREA = "Минская";
    public static final String HOUSE_COUNTRY = "Республика Беларусь";
    public static final String HOUSE_CITY = "Минск";
    public static final String HOUSE_STREET = "Центральная";
    public static final Integer HOUSE_NUMBER = 23;
    public static final LocalDateTime HOUSE_CREATE_DATE = LocalDateTime.of(2023, 1, 13, 18, 30, 0);
    public static final String UPDATE_HOUSE_AREA = "Могилёвская";
    public static final String UPDATE_HOUSE_CITY = "Шклов";
    public static final int UPDATE_HOUSE_NUMBER = 12;
    public static final UUID INCORRECT_UUID = UUID.fromString("2d915360-634a-4fe3-986b-c971da8dfaf5");
    public static final UUID HOUSE_UUID_FIRST_IN_DB = UUID.fromString("0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6");
    public static final UUID HOUSE_UUID_SECOND_IN_DB = UUID.fromString("9724b9b8-216d-4ab9-92eb-e6e06029580d");
    public static final UUID HOUSE_UUID_THIRD_IN_DB = UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13");
    public static final UUID HOUSE_UUID_FOURTH_IN_DB = UUID.fromString("d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14");
    public static final UUID HOUSE_UUID_FIFTH_IN_DB = UUID.fromString("e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15");

    public static House getFirstHouseInDB() {
        return HouseTestData.builder()
                .withId(1L)
                .withUuid(UUID.fromString("0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6"))
                .withArea("Гомельская")
                .withCountry("Беларусь")
                .withCity("Ельск")
                .withStreet("Ленина")
                .withNumber(2)
                .withCreateDate(LocalDateTime.of(2023, 12, 30, 12, 0, 0))
                .build()
                .getEntity();
    }

    public static House getSecondHouseInDB() {
        return HouseTestData.builder()
                .withUuid(UUID.fromString("9724b9b8-216d-4ab9-92eb-e6e06029580d"))
                .withArea("Гомельская")
                .withCountry("Беларусь")
                .withCity("Мозырь")
                .withStreet("Геологов")
                .withNumber(15)
                .build()
                .getEntity();
    }

    public static House getNewHouseForCreateWhichOwnPerson() {
        return HouseTestData.builder()
                .withUuid(UUID.fromString("c8adac62-9266-486f-8540-21e976b635b3"))
                .withArea("Гомельская")
                .withCountry("Республика Беларусь")
                .withCity("Наровля")
                .withStreet("Лесная")
                .withNumber(25)
                .build()
                .getEntity();
    }

    public static HouseRequest getNewHouseForCreate() {
        return HouseTestData.builder()
                .withArea("Гомельская")
                .withCountry("Республика Беларусь")
                .withCity("Логойск")
                .withStreet("Лесная")
                .withNumber(8)
                .build()
                .getRequestDto();
    }

    public static HouseRequest getHouseForUpdate() {
        return HouseTestData.builder()
                .withArea("Витебская")
                .withCountry("Беларусь")
                .withCity("Полоцк")
                .withStreet("Промышленная")
                .withNumber(5)
                .build()
                .getRequestDto();
    }
}