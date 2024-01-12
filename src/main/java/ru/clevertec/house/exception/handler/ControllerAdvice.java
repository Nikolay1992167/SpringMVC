package ru.clevertec.house.exception.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.house.exception.NotFoundException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

    private final IncorrectData incorrectData;

    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleNotFoundException(NotFoundException exception) {

        incorrectData.setErrorMessage(exception.getMessage());
        incorrectData.setErrorCode(HttpStatus.NOT_FOUND.toString());

        log.error(incorrectData.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(incorrectData);
    }

    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleThrowable(Throwable exception) {

        incorrectData.setErrorMessage(exception.getMessage());
        incorrectData.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());

        log.error(incorrectData.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(incorrectData);
    }
}
