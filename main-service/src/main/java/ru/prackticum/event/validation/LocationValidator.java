package ru.prackticum.event.validation;

import ru.prackticum.catergory.model.Location;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocationValidator implements ConstraintValidator<ValidLocation, Location> {

    @Override
    public boolean isValid(Location location, ConstraintValidatorContext context) {
        if (location == null) {
            return true;
        }
        return ((location.getLat() <= 90 && location.getLat() >= -90)
                && (location.getLon() <= 180 && location.getLon() >= -180));
    }

}