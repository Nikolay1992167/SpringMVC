package by.clevertec.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"passport_series", "passport_number"}))
public class Passport {

    @Column(name = "passport_series", nullable = false)
    String series;

    @Column(name = "passport_number", nullable = false)
    String number;
}
