package by.clevertec.dto.request;

import by.clevertec.entity.Person;
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
public class HouseRequest {

    private String id;
    private String area;
    private String country;
    private String city;
    private String street;
    private Integer number;

    @Builder.Default
    private List<Person> residents = new ArrayList<>();
    @Builder.Default
    private List<Person> owners = new ArrayList<>();
}
