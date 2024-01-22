package ru.clevertec.house.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import ru.clevertec.house.entity.listener.PersonListener;
import ru.clevertec.house.enums.Sex;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@EntityListeners(PersonListener.class)
@Table(name = "persons", uniqueConstraints = @UniqueConstraint(columnNames = {"passport_series", "passport_number"}))
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", unique = true, nullable = false)
    private UUID uuid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "sex")
    @Enumerated(value = EnumType.STRING)
    private Sex sex;

    @Embedded
    private Passport passport;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.REFRESH})
    private House house;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "houses_persons",
            joinColumns = @JoinColumn(name = "persons_id"),
            inverseJoinColumns = @JoinColumn(name = "houses_id"))
    private List<House> ownedHouses;
}