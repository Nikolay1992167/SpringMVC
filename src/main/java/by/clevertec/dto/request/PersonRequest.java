package by.clevertec.dto.request;

import by.clevertec.entity.House;
import by.clevertec.entity.Passport;
import by.clevertec.enums.Sex;
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
public class PersonRequest {

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String surname;

    @NotNull
    private Sex sex;

    @NotNull
    private Passport passport;

    @NotNull
    private House house;

    @Valid
    @Builder.Default
    private List<House> ownedHouses = new ArrayList<>();
}
