package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingServiceTest {

    private BookingService bookingService;

    private BookingRepository bookingRepository;

    private UserRepository userRepository;

    private ItemRepository itemRepository;
    private User booker1;
    private User owner;
    private Item item1;

    private BookingRequestDto bookingRequestDto;
    private LocalDateTime start;
    private LocalDateTime end;

    private Booking booking;

    @BeforeEach
    void init() {
        bookingRepository = Mockito.mock(BookingRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        booker1 = User.builder().id(1L).name("User1").email("user1@user.com").build();
        owner = User.builder().id(2L).name("User2").email("user2@user.com").build();
        item1 = Item.builder().id(1L).name("Дрель").description("Простая дрель").available(true).owner(owner).build();
        start = LocalDateTime.now();
        end = LocalDateTime.now().plusDays(1);
        bookingRequestDto = BookingRequestDto.builder().itemId(1L).start(start).end(end).build();
        booking = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .booker(booker1)
                .item(item1)
                .status(BookingStatus.WAITING)
                .build();
    }


    @Test
    void create() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booker1));

        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item1));

        Booking booking = BookingMapper.getBooking(bookingRequestDto);
        booking.setId(1L);
        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(booker1);
        booking.setItem(item1);

        Mockito.when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking);

        BookingDto bookingDto = bookingService.create(bookingRequestDto, booking.getId());

        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getItem().getName(), booking.getItem().getName());

        Mockito.verify(bookingRepository, Mockito.times(1)).save(Mockito.any(Booking.class));
    }

    @Test
    void updateBookingStatus() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(owner));

        Booking bookingFromDb = BookingMapper.getBooking(bookingRequestDto);

        bookingFromDb.setId(1L);
        bookingFromDb.setStatus(BookingStatus.WAITING);
        bookingFromDb.setBooker(booker1);
        bookingFromDb.setItem(item1);

        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(bookingFromDb));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(bookingFromDb);

        bookingService.updateBookingStatus(1L, "true", owner.getId());
        Mockito.verify(bookingRepository, Mockito.times(1)).save(Mockito.any());

    }

    @Test
    void getBooking() {

        Mockito.when(
                bookingRepository
                        .findById(Mockito.anyLong()))
                        .thenReturn(Optional.of(booking));

        bookingService.getBooking(1L, 1L);

        Mockito.verify(
                bookingRepository,
                Mockito.times(1))
                .findById(Mockito.anyLong());
    }

    @Test
    void getAllByBooker() {

        Mockito.when(
                userRepository
                        .findById(Mockito.anyLong()))
                        .thenReturn(Optional.of(booker1));

        Mockito.when(
                bookingRepository
                        .findAllByBookerOrderByStartDesc(Mockito.any(), Mockito.any()))
                .thenReturn(List.of(booking));

        bookingService.getAllByBooker( "ALL", 1L, 1, 1);

        Mockito.verify(
                bookingRepository,
                Mockito.times(1))
                .findAllByBookerOrderByStartDesc(Mockito.any(), Mockito.any());
    }

    @Test
    void getAllBookingsForAllItemsOfOwner() {

        Mockito.when(
                userRepository
                        .findById(Mockito.anyLong()))
                        .thenReturn(Optional.of(booker1));

        Mockito.when(
                bookingRepository
                        .findAllByItemOwnerOrderByStartDesc(Mockito.any(), Mockito.any()))
                        .thenReturn(List.of(booking));

        bookingService.getAllBookingsForAllItemsOfOwner( "ALL", 1L, 1, 1);

        Mockito.verify(
                bookingRepository,
                Mockito.times(1))
                .findAllByItemOwnerOrderByStartDesc(Mockito.any(), Mockito.any());
    }
}