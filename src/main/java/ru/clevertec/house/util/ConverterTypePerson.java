package ru.clevertec.house.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;
import ru.clevertec.house.enums.TypePerson;
import ru.clevertec.house.exception.ValidationException;

@Component
@Converter
public class ConverterTypePerson implements AttributeConverter<TypePerson, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TypePerson attribute) {
        return switch (attribute) {
            case OWNER  -> 1;
            case TENANT -> 2;
        };
    }

    @Override
    public TypePerson convertToEntityAttribute(Integer dbData) {
        return switch (dbData) {
            case 1 -> TypePerson.OWNER;
            case 2 -> TypePerson.TENANT;
            default -> throw ValidationException.of(TypePerson.class, dbData);
        };
    }
}
