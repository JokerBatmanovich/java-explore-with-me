package ru.prackticum.exception;

public class IncorrectEmailException extends RuntimeException {

    public IncorrectEmailException() {
        super("Длина домена не должна превышать 64 символа");
    }


}
