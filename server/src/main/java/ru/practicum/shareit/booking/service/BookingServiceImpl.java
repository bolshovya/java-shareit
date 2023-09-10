package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public BookingDto create(BookingRequestDto bookingRequestDto, Long userId) {
        log.info("BookingServiceImpl: сохранение бронирования: {}, для пользователя с id: {}", bookingRequestDto, userId);

        User booker = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Booking bookingToDb = BookingMapper.getBooking(bookingRequestDto);
        bookingToDb.setStatus(BookingState.WAITING);
        bookingToDb.setBooker(booker);

        Item item = itemRepository.findById(bookingRequestDto.getItemId()).orElseThrow(ItemNotFoundException::new);
        if (!item.getAvailable()) {
            throw new ItemValidationException("Элемент с id: " + item.getId() + " не доступен");
        }
        if (item.getOwner().getId().equals(userId)) {
            throw new ItemNotFoundException("Владелец не может бронировать свои элементы");
        }
        bookingToDb.setItem(item);
        return BookingMapper.getBookingDto(bookingRepository.save(bookingToDb));
    }


    @Transactional
    @Override
    public BookingDto updateBookingState(Long bookingId, String approved, Long userId) {
        User owner = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);

        User ownerBooking = booking.getItem().getOwner();

        if (!ownerBooking.getId().equals(userId)) {
            throw new BookingNotFoundException("Пользователь с id: " + userId + "не является владельцем.");
        }
        if (booking.getStatus() != BookingState.WAITING) {
            throw new BookingValidationException();
        }

        switch (approved.toLowerCase()) {
            case "true":
                booking.setStatus(BookingState.APPROVED);
                break;
            case "false":
                booking.setStatus(BookingState.REJECTED);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + approved);
        }
        return BookingMapper.getBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);

        Long ownerId = booking.getItem().getOwner().getId();
        Long bookerId = booking.getBooker().getId();

        if (!userId.equals(ownerId) && !userId.equals(bookerId)) {
            throw new BookingNotFoundException();
        }

        return BookingMapper.getBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllByBooker(String state, Long userId, Integer from, Integer size) {
        log.info("BookingServiceImpl: запрос всех бронирований({}) пользователя с id: {}", state, userId);
        User booker = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        LocalDateTime time = LocalDateTime.now();
        Pageable pageable = PageRequest.of(from / size, size);
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByBookerOrderByStartDesc(booker, pageable)
                        .stream().map(BookingMapper::getBookingDto).collect(Collectors.toList());

            case "PAST":
                return bookingRepository.findAllByBookerAndEndBeforeOrderByStartDesc(booker, time, pageable)
                        .stream()
                        .map(BookingMapper::getBookingDto)
                        .sorted(Comparator.comparing(BookingDto::getStart))
                        .collect(Collectors.toList());

            case "CURRENT":
                return bookingRepository.findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(booker, time, time, pageable)
                        .stream()
                        .map(BookingMapper::getBookingDto)
                        .sorted(Comparator.comparing(BookingDto::getStart))
                        .collect(Collectors.toList());

            case "FUTURE":
                return bookingRepository.findAllByBookerAndEndAfterOrderByStartDesc(booker, time, pageable)
                        .stream().map(BookingMapper::getBookingDto).collect(Collectors.toList());

            case "WAITING":

            case "REJECTED":
                return bookingRepository.findAllByBookerAndStatusOrderByStartDesc(booker, BookingState.valueOf(state), pageable)
                        .stream().map(BookingMapper::getBookingDto).collect(Collectors.toList());

            default:
                throw new UnknownStateException(state);
        }
    }

    @Override
    public List<BookingDto> getAllBookingsForAllItemsOfOwner(String state, Long userId, Integer from, Integer size) {
        log.info("BookingServiceImpl: запрос всех бронирований({}) вещей владельца с id: {}", state, userId);
        User owner = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        LocalDateTime time = LocalDateTime.now();
        Pageable pageable = PageRequest.of(from / size, size);
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByItemOwnerOrderByStartDesc(owner, pageable)
                        .stream().map(BookingMapper::getBookingDto).collect(Collectors.toList());

            case "PAST":
                return bookingRepository.findAllByItemOwnerAndEndBeforeOrderByStartDesc(owner, time, pageable)
                        .stream()
                        .map(BookingMapper::getBookingDto)
                        .sorted(Comparator.comparing(BookingDto::getStart))
                        .collect(Collectors.toList());

            case "CURRENT":
                return bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(owner, time, time, pageable)
                        .stream()
                        .map(BookingMapper::getBookingDto)
                        .sorted(Comparator.comparing(BookingDto::getStart))
                        .collect(Collectors.toList());

            case "FUTURE":
                return bookingRepository.findAllByItemOwnerAndEndAfterOrderByStartDesc(owner, time, pageable)
                        .stream().map(BookingMapper::getBookingDto).collect(Collectors.toList());

            case "WAITING":

            case "REJECTED":
                return bookingRepository.findAllByItemOwnerAndStatusOrderByStartDesc(owner, BookingState.valueOf(state), pageable)
                        .stream().map(BookingMapper::getBookingDto).collect(Collectors.toList());

            default:
                throw new UnknownStateException(state);
        }
    }
}