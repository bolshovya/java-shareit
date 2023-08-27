package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.List;

public interface BookingService {
    BookingDto create(BookingRequestDto bookingRequestDto, Long userId);

    BookingDto updateBookingStatus(Long bookingId, String approved, Long userId);

    BookingDto getBooking(Long bookingId, Long userId);

    List<BookingDto> getAllByBooker(String state, Long userId, Integer from, Integer size);

    List<BookingDto> getAllBookingsForAllItemsOfOwner(String state, Long userId, Integer from, Integer size);
}
