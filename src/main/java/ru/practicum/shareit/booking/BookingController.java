package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    /**
     * Добавление нового запроса на бронирование.
     * Запрос может быть создан любым пользователем, а затем подтверждён владельцем вещи.
     * Эндпоинт — POST /bookings.
     * После создания запрос находится в статусе WAITING — «ожидает подтверждения».
     */
    @PostMapping
    public BookingDto create(@Valid @RequestBody BookingRequestDto bookingRequestDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("BookingController POST: сохранение бронирования: {}, для пользователя в с id: {}", bookingRequestDto, userId);
        return bookingService.create(bookingRequestDto, userId);
    }

    /**
    * Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи.
    * Затем статус бронирования становится либо APPROVED, либо REJECTED.
    * Эндпоинт — PATCH /bookings/{bookingId}?approved={approved}, параметр approved может принимать значения true или false.
     */
    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(@PathVariable Long bookingId, @RequestParam String approved,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.updateBookingStatus(bookingId, approved, userId);
    }

    /**
    * Получение данных о конкретном бронировании (включая его статус).
    * Может быть выполнено либо автором бронирования, либо владельцем вещи, к которой относится бронирование.
    * Эндпоинт — GET /bookings/{bookingId}.
     */
    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBooking(bookingId, userId);
    }

    /**
     * Получение списка всех бронирований текущего пользователя.
     * Эндпоинт — GET /bookings?state={state}. Параметр state необязательный и по умолчанию равен ALL (англ. «все»).
     * Также он может принимать значения:
     * CURRENT (англ. «текущие»),
     * PAST (англ. «завершённые»),
     * FUTURE (англ. «будущие»),
     * WAITING (англ. «ожидающие подтверждения»),
     * REJECTED (англ. «отклонённые»).
     * Бронирования должны возвращаться отсортированными по дате от более новых к более старым.
     */
    @GetMapping
    public List<BookingDto> getAllByBooker(@RequestParam(value = "state", defaultValue = "ALL", required = false) String state,
                                            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("BookingController: запрос всех бронирований({}) пользователя с id: {}", state, userId);
        return bookingService.getAllByBooker(state, userId);
    }

    /**
     * Получение списка бронирований для всех вещей текущего пользователя.
     * Эндпоинт — GET /bookings/owner?state={state}.
     * Этот запрос имеет смысл для владельца хотя бы одной вещи.
     * Работа параметра state аналогична его работе в предыдущем сценарии.
     */
    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForAllItemsOfOwner(@RequestParam(value = "state", defaultValue = "ALL", required = false) String state,
                                           @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("BookingController: запрос всех бронирований({}) пользователя с id: {}", state, userId);
        return bookingService.getAllBookingsForAllItemsOfOwner(state, userId);
    }


}
