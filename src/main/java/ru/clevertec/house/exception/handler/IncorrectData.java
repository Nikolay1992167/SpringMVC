package ru.clevertec.house.exception.handler;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class IncorrectData {

    private String errorMessage;
    private String errorCode;
}