package ru.clevertec.house.mapper;


import ru.clevertec.house.dto.request.HouseRequest;
import ru.clevertec.house.dto.response.HouseResponse;
import ru.clevertec.house.entity.House;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface HouseMapper {

    HouseResponse toResponse(House house);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    House toHouse(HouseRequest houseRequest);
}