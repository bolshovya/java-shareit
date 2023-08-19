package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto create(BookingDto bookingDto, Long userId);

    BookingDto updateApproved(Long bookingId, Boolean approved, Long userId);

    BookingDto findById(Long bookingId, Long userId);

    List<BookingDto> findAllByBooker(Long userId);
}
