package ru.clevertec.house.dto.response;

import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Passport;
import ru.clevertec.house.enums.Sex;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss:SSS")
    private LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss:SSS")
    private LocalDateTime updateDate;
}
