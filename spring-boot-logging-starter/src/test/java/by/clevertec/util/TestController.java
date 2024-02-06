package by.clevertec.util;

import by.clevertec.annotation.Logging;

@Logging
public class TestController {

    public String test(String parameter, int value) {

        return  "You are using a " + parameter + " with the value " + value +".";
    }
}
