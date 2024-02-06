package util.initdata;

import ru.clevertec.house.enums.Sex;

import java.time.LocalDateTime;
import java.util.UUID;

public class ConstantsForPerson {

    public static final Long PERSON_ID = 2L;
    public static final UUID PERSON_UUID = UUID.fromString("5633c57e-703c-4199-b340-18421d88f330");
    public static final String PERSON_NAME = "Геннадий";
    public static final String PERSON_SURNAME = "Воронко";
    public static final Sex PERSON_SEX = Sex.MALE;
    public static final LocalDateTime PERSON_CREATE_DATE = LocalDateTime.of(2023, 1, 1, 19, 0, 0);
    public static final LocalDateTime PERSON_UPDATE_DATE = LocalDateTime.of(2023, 1, 13, 19,30, 0);
    public static final String UPDATE_PERSON_NAME = "Виктор";
    public static final String UPDATE_PERSON_SURNAME = "Строганов";
}
