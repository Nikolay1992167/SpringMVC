package ru.clevertec.house.exception;

public class ValidationException extends RuntimeException{

    public ValidationException(String message) {
        super(message);
    }

    public static ValidationException of(Class<?> clazz, Object field) {
        return new ValidationException("Parameter " + clazz.getSimpleName() + field + " specified incorrectly!");
    }
}