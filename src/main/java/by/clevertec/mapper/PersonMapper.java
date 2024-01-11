package by.clevertec.mapper;

import by.clevertec.dto.request.PersonRequest;
import by.clevertec.dto.response.PersonResponse;
import by.clevertec.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PersonMapper {

    PersonResponse toResponse(Person person);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Person toPerson(PersonRequest personRequest);
}
