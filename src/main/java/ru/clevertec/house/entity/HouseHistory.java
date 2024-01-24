package ru.clevertec.house.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import ru.clevertec.house.enums.TypePerson;
import ru.clevertec.house.util.ConverterTypePerson;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Table(name = "house_history")
public class HouseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private House house;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person person;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Convert(converter = ConverterTypePerson.class)
    @Column(name = "type_id")
    private TypePerson type;
}
