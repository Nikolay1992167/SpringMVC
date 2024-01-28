package util.initdata;

import java.time.LocalDateTime;
import java.util.UUID;

public class ConstantsForHouse {

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
    public static final UUID INCORRECT_UUID = UUID.fromString("2d915360-634a-4fe3-986b-c971da8dfaf5");
}
