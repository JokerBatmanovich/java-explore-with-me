package ru.prackticum.event.utils;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumController {

    public static <T extends Enum<T>> Set<T> convertToEnumList(Class<T> enumClass, List<String> enumStrings) {
        EnumSet<T> allEnumValues = EnumSet.allOf(enumClass);

        return enumStrings.stream()
                .filter(value -> allEnumValues.stream().anyMatch(enumValue -> enumValue.name().equals(value)))
                .map(value -> Enum.valueOf(enumClass, value))
                .collect(Collectors.toSet());
    }
}
