package by.clevertec.dao;

import by.clevertec.entity.House;

import java.util.List;
import java.util.UUID;

public interface HouseDao extends CrudDao<House, UUID>{

    List<House> findAllHouseWhenOwnPerson(UUID personId);
}
