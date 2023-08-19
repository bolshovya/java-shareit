package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException e) {
        log.debug("Пользователь не найден. Получен статус 404 {}", e.getMessage());
        return new ErrorResponse("UserNotFoundException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemNotFoundException(final ItemNotFoundException e) {
        log.debug("Предмет не найден. Получен статус 404 {}", e.getMessage());
        return new ErrorResponse("ItemNotFoundException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBookingNotFoundException(final BookingNotFoundException e) {
        log.debug("Бронирование не найдено. Получен статус 404 {}", e.getMessage());
        return new ErrorResponse("BookingNotFoundException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserValidationException(final UserValidationException e) {
        log.debug("UserValidationException. Получен статус 400 {}", e.getMessage());
        return new ErrorResponse("UserValidationException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleItemValidationException(final ItemValidationException e) {
        log.debug("ItemValidationException. Получен статус 400 {}", e.getMessage());
        return new ErrorResponse("ItemValidationException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBookingValidationException(final BookingValidationException e) {
        log.debug("BookingValidationException. Получен статус 400 {}", e.getMessage());
        return new ErrorResponse("BookingValidationException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final ConflictException e) {
        log.debug("ConflictException. Получен статус 409 {}", e.getMessage());
        return new ErrorResponse("ConflictException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.debug("MethodArgumentNotValidException. Получен статус 400 {}", e.getMessage());
        return new ErrorResponse("MethodArgumentNotValidException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final Exception e) {
        log.debug("Exception. Получен статус 500 {}", e.getMessage());
        return new ErrorResponse("Exception", e.getMessage());
    }

}
