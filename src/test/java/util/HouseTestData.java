package util;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.house.dto.request.HouseRequest;
import ru.clevertec.house.dto.response.HouseResponse;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.HouseHistory;
import ru.clevertec.house.entity.Person;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static util.initdata.ConstantsForHouse.HOUSE_AREA;
import static util.initdata.ConstantsForHouse.HOUSE_CITY;
import static util.initdata.ConstantsForHouse.HOUSE_COUNTRY;
import static util.initdata.ConstantsForHouse.HOUSE_CREATE_DATE;
import static util.initdata.ConstantsForHouse.HOUSE_ID;
import static util.initdata.ConstantsForHouse.HOUSE_NUMBER;
import static util.initdata.ConstantsForHouse.HOUSE_STREET;
import static util.initdata.ConstantsForHouse.HOUSE_UUID;

@Data
@Builder(setterPrefix = "with")
public class HouseTestData {

    @Builder.Default
    private Long id = HOUSE_ID;

    @Builder.Default
    private UUID uuid = HOUSE_UUID;

    @Builder.Default
    private String area = HOUSE_AREA;

    @Builder.Default
    private String country = HOUSE_COUNTRY;

    @Builder.Default
    private String city = HOUSE_CITY;

    @Builder.Default
    private String street = HOUSE_STREET;

    @Builder.Default
    private Integer number = HOUSE_NUMBER;

    @Builder.Default
    private LocalDateTime createDate = HOUSE_CREATE_DATE;

    @Builder.Default
    private Set<Person> residents = Set.of();

    @Builder.Default
    private Set<Person> owners = Set.of();

    @Builder.Default
    private Set<HouseHistory> houseHistories = Set.of();

    public House getEntity() {
        return new House(id, uuid, area, country, city, street, number, createDate, residents, owners, houseHistories);
    }

    public HouseRequest getRequestDto() {
        return new HouseRequest(area, country, city, street, number);
    }

    public Optional<House> getOptionalEntity() {
        return Optional.of(getEntity());
    }

    public HouseResponse getResponseDto() {
        return new HouseResponse(uuid, area, country, city, street, number, createDate);
    }
}