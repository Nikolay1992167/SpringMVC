package ru.clevertec.house.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import ru.clevertec.house.enums.TypePerson;

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

    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "person_type")
    private TypePerson type;
}
