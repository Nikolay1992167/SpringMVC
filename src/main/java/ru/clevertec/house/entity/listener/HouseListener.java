package ru.clevertec.house.entity.listener;

import ru.clevertec.house.entity.House;
import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;
import java.util.UUID;

public class HouseListener {

    @PrePersist
    public  void prePersist(House house){
        house.setUuid(UUID.randomUUID());
        house.setCreateDate(LocalDateTime.now());
    }
}
