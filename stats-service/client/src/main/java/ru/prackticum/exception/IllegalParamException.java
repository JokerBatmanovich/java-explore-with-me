package ru.prackticum.exception;

public class IllegalParamException extends RuntimeException {
    public IllegalParamException(String paramName, String paramValue) {
        super (String.format("Некорректное значение параметра %s: %s", paramName, paramValue));
    }
}
