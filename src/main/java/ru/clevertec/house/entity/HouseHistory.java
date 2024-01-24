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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import ru.clevertec.house.enums.TypePerson;
import ru.clevertec.house.util.ConverterTypePerson;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Table(name = "house_history")
public class HouseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private House house;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Person person;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Convert(converter = ConverterTypePerson.class)
    @Column(name = "type_id")
    private TypePerson type;
}
