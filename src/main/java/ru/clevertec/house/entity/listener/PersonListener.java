package ru.clevertec.house.entity.listener;

import ru.clevertec.house.entity.Person;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;
import java.util.UUID;

public class PersonListener {

    @PrePersist
    public void prePersist(Person person) {
        LocalDateTime now = LocalDateTime.now();
        person.setCreateDate(now);
        person.setUpdateDate(now);
        person.setUuid(UUID.randomUUID());
    }

    @PreUpdate
    public void preUpdate(Person person) {
        person.setUpdateDate(LocalDateTime.now());
    }
}
