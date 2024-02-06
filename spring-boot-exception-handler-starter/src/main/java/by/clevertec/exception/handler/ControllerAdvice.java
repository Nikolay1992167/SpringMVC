package by.clevertec.exception.handler;

import by.clevertec.exception.CheckEmptyException;
import by.clevertec.exception.HouseNotEmptyException;
import by.clevertec.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    /**
     * Handles {@link NotFoundException} and returns a 404 Not Found response with an error message.
     *
     * @param exception The NotFoundException to handle.
     * @return A ResponseEntity containing an {@link IncorrectData} object and a 404 status code.
     */
    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleNotFoundException(NotFoundException exception) {

        IncorrectData incorrectData = new IncorrectData();
        incorrectData.setErrorMessage(exception.getMessage());
        incorrectData.setErrorCode(HttpStatus.NOT_FOUND.toString());

        log.error(incorrectData.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(incorrectData);
    }

    /**
     * Handles {@link Throwable exception} and returns a 500 SERVER ERROR response with an error message.
     *
     * @param exception The Throwable to handle.
     * @return A ResponseEntity containing an {@link IncorrectData} object and a 500 status code.
     */
    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleThrowable(Throwable exception) {

        IncorrectData incorrectData = new IncorrectData();
        incorrectData.setErrorMessage(exception.getMessage());
        incorrectData.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());

        log.error(incorrectData.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(incorrectData);
    }

    /**
     * Handles {@link CheckEmptyException} and returns a 400 BAD REQUEST response with an error message.
     *
     * @param exception The CheckEmptyException to handle.
     * @return A ResponseEntity containing an {@link IncorrectData} object and a 400 status code.
     */
    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleCheckEmptyException(CheckEmptyException exception) {

        IncorrectData incorrectData = new IncorrectData();
        incorrectData.setErrorMessage(exception.getMessage());
        incorrectData.setErrorCode(HttpStatus.BAD_REQUEST.toString());

        log.error(incorrectData.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(incorrectData);
    }

    /**
     * Handles {@link HouseNotEmptyException} and returns a 400 CLIENT ERROR response with an error message.
     *
     * @param exception The HouseNotEmptyException to handle.
     * @return A ResponseEntity containing an {@link IncorrectData} object and a 409 status code.
     */
    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleHouseNotEmptyException(HouseNotEmptyException exception) {

        IncorrectData incorrectData = new IncorrectData();
        incorrectData.setErrorMessage(exception.getMessage());
        incorrectData.setErrorCode(HttpStatus.CONFLICT.toString());

        log.error(incorrectData.toString());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(incorrectData);
    }
}