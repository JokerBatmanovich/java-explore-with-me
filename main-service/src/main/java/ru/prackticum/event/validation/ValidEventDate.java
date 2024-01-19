package ru.prackticum.event.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EventDateValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEventDate {
    String message() default "Время проведения мероприятия должно быть не раньше, чем через 2 часа.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int hoursCount() default 0;
}