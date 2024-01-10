package ru.prackticum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(IncorrectParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameterException(final IncorrectParameterException error) {
        log.info("Incorrect parameter error: {}", error.getMessage());
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(error.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

}
