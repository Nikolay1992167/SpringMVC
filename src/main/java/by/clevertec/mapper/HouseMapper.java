package by.clevertec.mapper;


import by.clevertec.dto.request.HouseRequest;
import by.clevertec.dto.response.HouseResponse;
import by.clevertec.entity.House;
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
