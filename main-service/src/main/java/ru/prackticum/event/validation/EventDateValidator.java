package ru.prackticum.event.validation;


import ru.prackticum.exception.IncorrectEventDateException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EventDateValidator implements ConstraintValidator<ValidEventDate, LocalDateTime> {
    private int hoursCount;

    @Override
    public void initialize(ValidEventDate annotation) {
        this.hoursCount = annotation.hoursCount();
    }

    @Override
    public boolean isValid(LocalDateTime eventDate, ConstraintValidatorContext context) {
        if (eventDate == null) {
            return true;
        }
        if (!eventDate.isAfter(LocalDateTime.now().plusHours(hoursCount))) {
            throw new IncorrectEventDateException(eventDate);
        }
        return true;
    }

}