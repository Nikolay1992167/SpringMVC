package by.clevertec.exception;

public class CheckEmptyException extends RuntimeException{

    public CheckEmptyException(String message) {
        super(message);
    }

    public static CheckEmptyException of(Class<?> clazz, Object field) {
        return new CheckEmptyException("In " + clazz.getSimpleName() + field + " can't empty!");
    }
}
