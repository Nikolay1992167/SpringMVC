package by.clevertec.exception.handler;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class IncorrectData {

    private String errorMessage;
    private String errorCode;
}