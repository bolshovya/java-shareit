package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
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
    public BookingDto create(BookingDto bookingDto, Long userId) {
        log.info("BookingServiceImpl: сохранение бронирования: {}, для пользователя с id: {}", bookingDto, userId);
        validation(bookingDto);
        bookingDto.setStatus(BookingStatus.WAITING);
        User booker = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Booking bookingToDb = BookingMapper.getBooking(bookingDto);
        bookingToDb.setBooker(booker);

        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(ItemNotFoundException::new);
        if (!item.getAvailable()) {
            throw new ItemValidationException("Элемент с id: " + item.getId() + " не доступен");
        }
        bookingToDb.setItem(item);
        return BookingMapper.getBookingDto(bookingRepository.save(bookingToDb));
        /*
        bookingDto.setBooker(booker);
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(ItemNotFoundException::new);
        if (!item.getAvailable()) {
            throw new ItemValidationException("Элемент с id: " + item.getId() + " не доступен");
        }
        bookingDto.setItem(item);
        Booking createdBooking = bookingRepository.save(BookingMapper.getBooking(bookingDto));
        return BookingMapper.getBookingDto(createdBooking);

         */
    }

    private void validation(BookingDto bookingDto) {
        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();
        if (start.isAfter(end) || start.isEqual(end)) {
            throw new BookingValidationException();
        }
    }

    @Override
    public BookingDto updateApproved(Long bookingId, Boolean approved, Long userId) {
        // User booker = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);
        User booker = booking.getBooker();
        if (approved == true) {
            booking.setStatus(BookingStatus.APPROVED);
        }
        return BookingMapper.getBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto findById(Long bookingId, Long userId) {
        return BookingMapper.getBookingDto(bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new));
    }

    @Override
    public List<BookingDto> findAllByBooker(Long userId) {
        User booker = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return bookingRepository.findAllByBookerOrderByStartDesc(booker).stream().map(BookingMapper::getBookingDto).collect(Collectors.toList());
    }

    /*
    public List<BookingDto> findAllByBookerAndStatus(String state, Long userId) {
        User booker = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByBookerOrderByStartDesc(booker).stream().map(BookingMapper::getBookingDto).collect(Collectors.toList());
            case "CURRENT":
                return

            case "PAST":

            case "FUTURE":

            case "WAITING":

            case "REJECTED":

            default:
                throw new BookingValidationException();
        }
    }

     */
}
