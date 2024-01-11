package by.clevertec.dto.request;

import by.clevertec.entity.Person;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseRequest {

    @NotBlank
    @Size(max = 50)
    private String area;

    @NotBlank
    @Size(max = 50)
    private String country;

    @NotBlank
    @Size(max = 50)
    private String city;

    @NotBlank
    @Size(max = 50)
    private String street;

    @NotNull
    @Size(max = 5)
    private Integer number;

    @Valid
    @Builder.Default
    private List<Person> residents = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<Person> owners = new ArrayList<>();
}
