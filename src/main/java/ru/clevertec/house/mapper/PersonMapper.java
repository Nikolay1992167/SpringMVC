package ru.clevertec.house.mapper;

import ru.clevertec.house.dto.request.PersonRequest;
import ru.clevertec.house.dto.response.PersonResponse;
import ru.clevertec.house.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PersonMapper {

    @Mapping(source = "house.uuid", target = "houseUUID")
    PersonResponse toResponse(Person person);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "house", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Person toPerson(PersonRequest personRequest);
}