package by.clevertec.dao;

import by.clevertec.entity.House;

import java.util.List;

public interface HouseDao extends CrudDao<House, Long>{

    List<House> findAllHouseWhenOwnPerson(Long personId);
}
