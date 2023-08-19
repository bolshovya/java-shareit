package ru.practicum.shareit.exception;

public class BookingValidationException extends RuntimeException{


    public BookingValidationException() {
    }

    public BookingValidationException(String message) {
        super(message);
    }
}
