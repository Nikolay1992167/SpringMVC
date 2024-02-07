package by.clevertec.util;

import by.clevertec.annotation.Logging;

@Logging
public class TestHandler {

    public String test(String parameter, int value) {

        return  "You are using a " + parameter + " with the value " + value +".";
    }
}
