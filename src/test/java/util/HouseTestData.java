package util;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.house.dto.request.HouseRequest;
import ru.clevertec.house.dto.response.HouseResponse;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Person;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    private List<Person> residents = List.of();

    @Builder.Default
    private List<Person> owners = List.of();

    public House getEntity() {
        return new House(id, uuid, area, country, city, street, number, createDate, residents, owners);
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

    public List<House> getListOfHouse() {
        return  List.of(HouseTestData.builder().build().getEntity(),
                HouseTestData.builder()
                        .withId(11L)
                        .withUuid(UUID.fromString("79e209ea-3ab2-4025-9bad-6e5384dfa38b"))
                        .withArea("Гомельская")
                        .withCountry("Республика Беларусь")
                        .withCity("Лельчицы")
                        .withStreet("Комсомольская")
                        .withNumber(29)
                        .withCreateDate(LocalDateTime.of(2002, 5, 19, 9, 30, 0))
                        .build()
                        .getEntity());
    }
}