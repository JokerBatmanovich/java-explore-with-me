package ru.prackticum.exception;

public class RequestNotFoundException extends RuntimeException {

    public RequestNotFoundException(Long id) {
        super("Запрос с ID=" + id + " не найден.");
    }

}
