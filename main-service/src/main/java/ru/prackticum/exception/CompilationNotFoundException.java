package ru.prackticum.exception;

public class CompilationNotFoundException extends RuntimeException {

    public CompilationNotFoundException(Long id) {
        super("Подборка с ID=" + id + " не найдена.");
    }

}
