package by.clevertec.entity.listener;

import by.clevertec.entity.House;
import jakarta.persistence.PrePersist;

import java.util.UUID;

public class HouseListener {

    @PrePersist
    public  void prePersist(House house){
        house.setUuid(UUID.randomUUID());
    }
}
