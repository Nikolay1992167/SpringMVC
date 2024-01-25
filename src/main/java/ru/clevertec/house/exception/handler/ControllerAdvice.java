package ru.clevertec.house.exception.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.house.exception.CheckEmptyException;
import ru.clevertec.house.exception.HouseNotEmptyException;
import ru.clevertec.house.exception.NotFoundException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleNotFoundException(NotFoundException exception) {

        IncorrectData incorrectData = new IncorrectData();
        incorrectData.setErrorMessage(exception.getMessage());
        incorrectData.setErrorCode(HttpStatus.NOT_FOUND.toString());

        log.error(incorrectData.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(incorrectData);
    }

    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleThrowable(Throwable exception) {

        IncorrectData incorrectData = new IncorrectData();
        incorrectData.setErrorMessage(exception.getMessage());
        incorrectData.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());

        log.error(incorrectData.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(incorrectData);
    }

    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleCheckEmptyException(CheckEmptyException exception) {

        IncorrectData incorrectData = new IncorrectData();
        incorrectData.setErrorMessage(exception.getMessage());
        incorrectData.setErrorCode(HttpStatus.BAD_REQUEST.toString());

        log.error(incorrectData.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(incorrectData);
    }

    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleHouseNotEmptyException(HouseNotEmptyException exception) {

        IncorrectData incorrectData = new IncorrectData();
        incorrectData.setErrorMessage(exception.getMessage());
        incorrectData.setErrorCode(HttpStatus.CONFLICT.toString());

        log.error(incorrectData.toString());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(incorrectData);
    }
}