package ru.clevertec.house.exception.handler;

import by.clevertec.exception.CheckEmptyException;
import by.clevertec.exception.HouseNotEmptyException;
import by.clevertec.exception.NotFoundException;
import by.clevertec.exception.handler.ControllerAdvice;
import by.clevertec.exception.handler.IncorrectData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.clevertec.house.entity.Person;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.initdata.TestDataForPerson.PERSON_UUID;

@ExtendWith(MockitoExtension.class)
class ControllerAdviceTest {

    @InjectMocks
    private ControllerAdvice controllerAdvice;

    @Test
    void shouldReturnHandleNotFoundException() {
        // given
        UUID personUuid = PERSON_UUID;
        NotFoundException exception = NotFoundException.of(Person.class, personUuid);

        // when
        ResponseEntity<IncorrectData> response = controllerAdvice.handleNotFoundException(exception);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Person with " + personUuid + " not found!", Objects.requireNonNull(response.getBody()).getErrorMessage());
    }

    @Test
    void handleThrowable() {
        // given
        Throwable exception = new Throwable("Different exception!");

        // when
        ResponseEntity<IncorrectData> response = controllerAdvice.handleThrowable(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Different exception!", Objects.requireNonNull(response.getBody()).getErrorMessage());
    }

    @Test
    void handleCheckEmptyException() {
        // given
        String fields = "value fields";
        CheckEmptyException exception = CheckEmptyException.of(Person.class, fields);

        // when
        ResponseEntity<IncorrectData> response = controllerAdvice.handleCheckEmptyException(exception);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("In Person" + fields + " can't empty!", Objects.requireNonNull(response.getBody()).getErrorMessage());
    }

    @Test
    void handleHouseNotEmptyException() {
        // given
        HouseNotEmptyException exception = new HouseNotEmptyException("There are tenants in the house!");

        // when
        ResponseEntity<IncorrectData> response = controllerAdvice.handleHouseNotEmptyException(exception);

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("There are tenants in the house!", Objects.requireNonNull(response.getBody()).getErrorMessage());
    }
}