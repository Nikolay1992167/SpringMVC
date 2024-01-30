package util;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.house.entity.Passport;

import static util.initdata.TestDataForPassport.PASSPORT_NUMBER;
import static util.initdata.TestDataForPassport.PASSPORT_SERIES;

@Data
@Builder(setterPrefix = "with")
public class PassportTestData {

    @Builder.Default
    String series = PASSPORT_SERIES;

    @Builder.Default
    String number = PASSPORT_NUMBER;

    public Passport getPassport() {
        return new Passport(series, number);
    }
}