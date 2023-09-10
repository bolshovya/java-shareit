package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.UnknownStateException;

import java.util.List;

import static ru.practicum.shareit.utils.Constant.USER_ID;

@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(
            @RequestBody BookingRequestDto bookingRequestDto,
            @RequestHeader(USER_ID) Long userId
    ) {
        log.info("BookingController POST: сохранение бронирования: {}, для пользователя в с id: {}", bookingRequestDto, userId);
        return bookingService.create(bookingRequestDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingState(
            @PathVariable Long bookingId,
            @RequestParam String approved,
            @RequestHeader(USER_ID) Long userId
    ) {
        return bookingService.updateBookingState(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(
            @PathVariable Long bookingId,
            @RequestHeader(USER_ID) Long userId
    ) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllByBooker(
            @RequestParam(value = "state", defaultValue = "ALL") String state,
            @RequestHeader(USER_ID) Long userId,
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        log.info("BookingController: запрос всех бронирований({}) пользователя с id: {}", state, userId);
        return bookingService.getAllByBooker(state, userId, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForAllItemsOfOwner(
            @RequestParam(value = "state", defaultValue = "ALL") String state,
            @RequestHeader(USER_ID) Long userId,
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        log.info("BookingController: запрос всех бронирований({}) пользователя с id: {}", state, userId);
        return bookingService.getAllBookingsForAllItemsOfOwner(state, userId, from, size);
    }


}