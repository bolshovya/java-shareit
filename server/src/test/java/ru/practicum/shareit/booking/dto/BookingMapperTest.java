package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

class BookingMapperTest {

    @Test
    void getBookingDto() {

        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(Item.builder()
                        .id(1L)
                        .name("Item")
                        .description("description")
                        .owner(User.builder().id(1L).build())
                        .build())
                .booker(User.builder().id(1L).build())
                .status(BookingState.WAITING)
                .build();

        BookingDto bookingDto = BookingMapper.getBookingDto(booking);
    }

    @Test
    void getBooking() {

        BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();

        Booking booking = BookingMapper.getBooking(bookingRequestDto);

    }
}