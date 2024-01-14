package ru.clevertec.house.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Passport {

    @Column(name = "passport_series", nullable = false)
    String series;

    @Column(name = "passport_number", nullable = false)
    String number;
}
