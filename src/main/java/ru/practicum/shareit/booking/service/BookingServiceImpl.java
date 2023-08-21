package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

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
        validation(bookingRequestDto);

        User booker = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Booking bookingToDb = BookingMapper.getBooking(bookingRequestDto);
        bookingToDb.setStatus(BookingStatus.WAITING);
        bookingToDb.setBooker(booker);

        Item item = itemRepository.findById(bookingRequestDto.getItemId()).orElseThrow(ItemNotFoundException::new);
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

    private void validation(BookingRequestDto bookingRequestDto) {
        LocalDateTime start = bookingRequestDto.getStart();
        LocalDateTime end = bookingRequestDto.getEnd();
        if (start.isAfter(end) || start.isEqual(end)) {
            throw new BookingValidationException();
        }
    }

    @Override
    public BookingDto updateBookingStatus(Long bookingId, String approved, Long userId) {
        User booker = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);

        User ownerBooking = booking.getItem().getOwner();

        if (!userId.equals(ownerBooking.getId())) {
            throw new BookingNotFoundException();
        }

        switch (approved.toLowerCase()) {
            case "true":
                booking.setStatus(BookingStatus.APPROVED);
                break;
            case "false":
                booking.setStatus(BookingStatus.REJECTED);
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
    public List<BookingDto> getAllByBooker(String state, Long userId) {
        log.info("BookingServiceImpl: запрос всех бронирований({}) пользователя с id: {}", state, userId);
        User booker = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        LocalDateTime time = LocalDateTime.now();

        switch (state) {
            case "ALL":
                return bookingRepository.findAllByBookerOrderByStartDesc(booker)
                        .stream().map(BookingMapper::getBookingDto).collect(Collectors.toList());

            case "CURRENT":
                return bookingRepository.findAllByBookerAndEndBeforeOrderByStartDesc(booker, time)
                        .stream().map(BookingMapper::getBookingDto).collect(Collectors.toList());

            case "PAST":
                return bookingRepository.findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(booker, time, time)
                        .stream().map(BookingMapper::getBookingDto).collect(Collectors.toList());

            case "FUTURE":
                return bookingRepository.findAllByBookerAndEndAfterOrderByStartDesc(booker, time)
                        .stream().map(BookingMapper::getBookingDto).collect(Collectors.toList());

            case "WAITING":
                return bookingRepository.findAllByBookerAndStatusOrderByStartDesc(booker, BookingStatus.valueOf(state))
                        .stream().map(BookingMapper::getBookingDto).collect(Collectors.toList());

            case "REJECTED":
                return bookingRepository.findAllByBookerAndStatusOrderByStartDesc(booker, BookingStatus.valueOf(state))
                        .stream().map(BookingMapper::getBookingDto).collect(Collectors.toList());

            default:
                throw new UnknownStateException(state);
        }
    }


}
