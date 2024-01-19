package ru.prackticum.exception;

public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException(Long id) {
        super("Событие с ID=" + id + " не найдено.");
    }
    public EventNotFoundException() {
        super("Событие не найдено.");
    }

}
