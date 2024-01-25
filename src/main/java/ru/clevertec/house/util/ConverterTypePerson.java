package ru.clevertec.house.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;
import ru.clevertec.house.enums.TypePerson;
import ru.clevertec.house.exception.ValidationException;

@Component
@Converter
public class ConverterTypePerson implements AttributeConverter<TypePerson, Integer> {

    /**
     * Converts a type person to a number.
     *
     * @param attribute  the entity attribute value to be converted.
     * @return the number corresponding to the type.
     */
    @Override
    public Integer convertToDatabaseColumn(TypePerson attribute) {
        return switch (attribute) {
            case OWNER  -> 1;
            case TENANT -> 2;
        };
    }

    /**
     *Converts a number to a type person.
     *
     * @param dbData  the data from the database column to be converted.
     * @return TypePerson corresponding to the number.
     */
    @Override
    public TypePerson convertToEntityAttribute(Integer dbData) {
        return switch (dbData) {
            case 1 -> TypePerson.OWNER;
            case 2 -> TypePerson.TENANT;
            default -> throw ValidationException.of(TypePerson.class, dbData);
        };
    }
}
