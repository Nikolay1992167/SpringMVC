package ru.clevertec.house.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.house.entity.Passport;
import ru.clevertec.house.enums.Sex;

import java.time.LocalDateTime;
import java.util.UUID;

import static ru.clevertec.house.util.Constants.PATTERN_FOR_DATA;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonResponse {

    private UUID uuid;
    private String name;
    private String surname;
    private Sex sex;
    private Passport passport;
    private UUID houseUUID;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_FOR_DATA)
    private LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_FOR_DATA)
    private LocalDateTime updateDate;
}
