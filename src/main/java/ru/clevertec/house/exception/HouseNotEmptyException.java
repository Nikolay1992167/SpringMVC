package ru.clevertec.house.exception;

public class HouseNotEmptyException extends RuntimeException {

    public HouseNotEmptyException(String message) {
        super(message);
    }
}
