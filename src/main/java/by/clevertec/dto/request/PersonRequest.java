package by.clevertec.dto.request;

import by.clevertec.entity.House;
import by.clevertec.entity.Passport;
import by.clevertec.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonRequest {

    private String id;
    private String name;
    private String surname;
    private Sex sex;
    private Passport passport;
    private House house;

    @Builder.Default
    private List<House> residents = new ArrayList<>();
}
