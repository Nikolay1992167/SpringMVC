package by.clevertec.dto.response;

import by.clevertec.entity.Person;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HouseResponse {

    private UUID uuid;
    private String area;
    private String country;
    private String city;
    private String street;
    private Integer number;

    @JsonProperty("create_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss:SSS")
    private LocalDateTime createDate;

    @Builder.Default
    private List<Person> residents = new ArrayList<>();
    @Builder.Default
    private List<Person> owners = new ArrayList<>();
}
