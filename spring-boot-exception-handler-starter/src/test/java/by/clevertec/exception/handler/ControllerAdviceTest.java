package by.clevertec.exception.handler;

import by.clevertec.exception.CheckEmptyException;
import by.clevertec.exception.HouseNotEmptyException;
import by.clevertec.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ControllerAdviceTest {

    @InjectMocks
    private ControllerAdvice controllerAdvice;

    @Test
    void shouldReturnHandleNotFoundException() {
        // given
        String message = "Entity not found!";
        NotFoundException exception = new NotFoundException(message);

        // when
        ResponseEntity<IncorrectData> response = controllerAdvice.handleNotFoundException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(Objects.requireNonNull(response.getBody()).getErrorMessage()).isEqualTo(message);
        assertThat(response.getBody().getErrorCode()).isEqualTo(HttpStatus.NOT_FOUND.toString());
    }

    @Test
    void handleThrowable() {
        // given
        String message = "Server error.";
        Throwable exception = new Throwable(message);

        // when
        ResponseEntity<IncorrectData> response = controllerAdvice.handleThrowable(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(Objects.requireNonNull(response.getBody()).getErrorMessage()).isEqualTo(message);
        assertThat(response.getBody().getErrorCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    @Test
    void handleCheckEmptyException() {
        // given
        String message = "Data cannot have empty values!";
        CheckEmptyException exception = new CheckEmptyException(message);

        // when
        ResponseEntity<IncorrectData> response = controllerAdvice.handleCheckEmptyException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(Objects.requireNonNull(response.getBody()).getErrorMessage()).isEqualTo(message);
        assertThat(response.getBody().getErrorCode()).isEqualTo(HttpStatus.BAD_REQUEST.toString());
    }

    @Test
    void handleHouseNotEmptyException() {
        // given
        String message = "There are tenants in the house!";
        HouseNotEmptyException exception = new HouseNotEmptyException(message);

        // when
        ResponseEntity<IncorrectData> response = controllerAdvice.handleHouseNotEmptyException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(Objects.requireNonNull(response.getBody()).getErrorMessage()).isEqualTo(message);
        assertThat(response.getBody().getErrorCode()).isEqualTo(HttpStatus.CONFLICT.toString());
    }
}