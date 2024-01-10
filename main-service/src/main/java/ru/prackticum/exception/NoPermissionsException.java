package ru.prackticum.exception;

public class NoPermissionsException extends RuntimeException {

    public NoPermissionsException(Long userId, Long eventId) {
        super(String.format("У пользователя с ID=%d нет прав на редактирование события с ID=%d", userId, eventId));
    }


}
