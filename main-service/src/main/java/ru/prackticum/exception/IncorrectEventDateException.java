package ru.prackticum.exception;

import java.time.LocalDateTime;

public class IncorrectEventDateException extends RuntimeException {

    public IncorrectEventDateException(LocalDateTime eventDate) {
        super("Некорректное время проведения события: \"" + eventDate
                + "\". Мероприятие должно начаться не раньше, чем через 2 часа.");
    }

}
