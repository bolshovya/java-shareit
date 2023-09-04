package ru.practicum.shareit.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorHandlerTest {

    private ErrorHandler errorHandler;

    @BeforeEach
    void init() {
        errorHandler = new ErrorHandler();
    }

    @Test
    void handleUserNotFoundException() {
        UserNotFoundException userNotFoundException = new UserNotFoundException("UserNotFoundException");
        ErrorResponse errorResponse = errorHandler.handleUserNotFoundException(userNotFoundException);

        assertEquals("UserNotFoundException", errorResponse.getError());

        UserNotFoundException userNotFoundExceptionSimple = new UserNotFoundException();
    }

    @Test
    void handleItemNotFoundException() {
        ItemNotFoundException itemNotFoundException = new ItemNotFoundException("ItemNotFoundException");
        ErrorResponse errorResponse = errorHandler.handleItemNotFoundException(itemNotFoundException);

        assertEquals("ItemNotFoundException", errorResponse.getError());

        ItemNotFoundException itemNotFoundExceptionSimple = new ItemNotFoundException();
    }

    @Test
    void handleBookingNotFoundException() {
        BookingNotFoundException bookingNotFoundException = new BookingNotFoundException("BookingNotFoundException");
        ErrorResponse errorResponse = errorHandler.handleBookingNotFoundException(bookingNotFoundException);

        assertEquals("BookingNotFoundException", errorResponse.getError());

        BookingNotFoundException bookingNotFoundExceptionSimple = new BookingNotFoundException();
    }

    @Test
    void handleItemRequestNotFoundException() {
        ItemRequestNotFoundException itemRequestNotFoundException = new ItemRequestNotFoundException("Запрос не найден");
        ErrorResponse errorResponse = errorHandler.handleItemRequestNotFoundException(itemRequestNotFoundException);

        assertEquals("Запрос не найден", errorResponse.getDescription());

        ItemRequestNotFoundException itemRequestNotFoundExceptionSimple = new ItemRequestNotFoundException();

    }

    @Test
    void handleUserValidationException() {
        UserValidationException userValidationException = new UserValidationException("UserValidationEcception");
        ErrorResponse errorResponse = errorHandler.handleUserValidationException(userValidationException);

        assertEquals("UserValidationException", errorResponse.getError());

        UserValidationException userValidationExceptionSimple = new UserValidationException();
    }

    @Test
    void handleItemValidationException() {
        ItemValidationException itemValidationExceptionSimple = new ItemValidationException();

        ItemValidationException itemValidationException = new ItemValidationException("ItemValidationException");
        ErrorResponse errorResponse = errorHandler.handleItemValidationException(itemValidationException);

        assertEquals("ItemValidationException", errorResponse.getError());
    }

    @Test
    void handleBookingValidationException() {
        BookingValidationException bookingValidationExceptionSimple = new BookingValidationException();

        BookingValidationException bookingValidationException = new BookingValidationException("BookingValidationException");
        ErrorResponse errorResponse = errorHandler.handleBookingValidationException(bookingValidationException);

        assertEquals("BookingValidationException", errorResponse.getError());
    }

    @Test
    void handleConflictException() {
        ConflictException conflictExceptionSimple = new ConflictException();

        ConflictException conflictException = new ConflictException("ConflictException");
        ErrorResponse errorResponse = errorHandler.handleConflictException(conflictException);

        assertEquals("ConflictException", errorResponse.getError());
    }

    @Test
    void handleUnknownStateException() {
        UnknownStateException unknownStateExceptionSimple = new UnknownStateException();

        UnknownStateException unknownStateException = new UnknownStateException("UnknownStateException");
        ErrorResponseSingle errorResponse = errorHandler.handleUnknownStateException(unknownStateException);

        assertEquals("Unknown state: UnknownStateException", errorResponse.getError());
    }


}