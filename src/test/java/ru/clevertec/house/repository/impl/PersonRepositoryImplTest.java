package ru.clevertec.house.repository.impl;

import config.DataBaseConfigForDaoIT;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.clevertec.house.repository.PersonRepository;
import ru.clevertec.house.entity.Person;
import util.PersonTestData;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(DataBaseConfigForDaoIT.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PersonRepositoryImplTest {
/*
    private PersonRepository personRepository;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    void setup() {
        personRepository = new PersonDaoImpl(sessionFactory);
    }

    @Test
    void findById() {
        // given
        UUID personUUID = UUID.fromString("9aa78d35-fb66-45a6-8570-f81513ef8272");
        String expected = "Марина";

        // when
        Optional<Person> actual = personRepository.findById(personUUID);

        //then
        actual.ifPresent(house ->
                assertThat(actual.get().getName())
                        .isEqualTo(expected));
    }

    @Test
    void findAll() {
        // given
        int expectedSize = 9;
        int pageNumber = 1;
        int pageSize = 10;

        // when
        List<Person> actual = personRepository.findAll(pageNumber, pageSize);

        // then
        assertThat(actual.size()).isEqualTo(expectedSize);
    }

    @Test
    void save() {
        // given
        Person expected = PersonTestData.builder()
                .withId(null)
                .build()
                .getEntity();

        // when
        Person actual = personRepository.save(expected);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void update() {
        // given
        String expected = "Геннадий";
        Person personToUpdate = PersonTestData.builder()
                //.withId(2L)
                .build()
                .getEntity();

        // when
        Person actual = personRepository.update(personToUpdate);

        assertThat(actual.getName()).isEqualTo(expected);
    }

    @Test
    void delete() {
        // given
        UUID personUUID = UUID.fromString("9aa78d35-fb66-45a6-8570-f81513ef8272");
        String expected = "Марина";

        // when
        Optional<Person> actual = personRepository.delete(personUUID);

        // then
        actual.ifPresent(house ->
                assertThat(actual.get().getName())
                        .isEqualTo(expected));
    }*/
}