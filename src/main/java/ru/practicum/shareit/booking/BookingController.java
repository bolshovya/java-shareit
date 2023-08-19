package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BookingValidationException;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@Valid @RequestBody BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("BookingController POST: сохранение бронирования: {}, для пользователя в с id: {}", bookingDto, userId);
        return bookingService.create(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateApproved(@PathVariable Long bookingId, @RequestParam Boolean approved,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.updateApproved(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> findAllByBooker(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.findAllByBooker(userId);
    }

    /*
    @GetMapping
    public List<BookingDto> findAllByBookerAndState(@RequestParam String state, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.findAllByBookerAndState(state, userId);
    }

     */

}
