package ru.prackticum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException error) {
        log.info("User not found: {}", error.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, error);
    }

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEventNotFoundException(final EventNotFoundException error) {
        log.info("Event not found: {}", error.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, error);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCategoryNotFoundException(final CategoryNotFoundException error) {
        log.info("User not found: {}", error.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException error) {
        log.info("Bad request error: {}", error.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, error);
    }

    @ExceptionHandler(NoPermissionsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoPermissionsException(final NoPermissionsException error) {
        log.info("No permissions error: {}", error.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, error);
    }

    @ExceptionHandler(IncorrectEventDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectEventDateException(final IncorrectEventDateException error) {
        log.info("Bad request error: {}", error.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, error);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleForbiddenException(final ForbiddenException error) {
        log.info("Conflict error: {}", error.getMessage());
        return buildResponse(HttpStatus.CONFLICT, error);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final ConflictException error) {
        log.info("Conflict error: {}", error.getMessage());
        return buildResponse(HttpStatus.CONFLICT, error);
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDateTimeParseException(final DateTimeParseException error) {
        log.info("Incorrect timestamp format: {}", error.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException error) {
        log.info("Validation error: {}", error.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, error);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(final DataIntegrityViolationException error) {
        log.info("Validation error: {}", error.getMessage());
        return buildResponse(HttpStatus.CONFLICT, error);
    }

    @ExceptionHandler(IncorrectRangeParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectRangeParameterException(final IncorrectRangeParameterException error) {
        log.info("Bad request: {}", error.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, error);
    }

    @ExceptionHandler(ConnectException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleConnectException(final ConnectException error) {
        log.info("Connection error: {}", error.getMessage());
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, error);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(final IllegalArgumentException error) {
        log.info("Validation error: {}", error.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, error);
    }

    @ExceptionHandler(IncorrectEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectEmailException(final IncorrectEmailException error) {
        log.info("Incorrect email error: {}", error.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, error);
    }


    private ErrorResponse buildResponse(HttpStatus status, Exception error) {
        return ErrorResponse.builder()
                .status(status.toString())
                .reason(status.getReasonPhrase())
                .message(error.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

}
