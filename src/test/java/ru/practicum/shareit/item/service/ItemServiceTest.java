package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ItemServiceTest {

    private ItemService itemService;
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private CommentRepository commentRepository;
    private ItemRequestRepository itemRequestRepository;

    @BeforeEach
    void init() {
        itemRepository = Mockito.mock(ItemRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        bookingRepository = Mockito.mock(BookingRepository.class);
        commentRepository = Mockito.mock(CommentRepository.class);
        itemRequestRepository = Mockito.mock(ItemRequestRepository.class);

        itemService = new ItemServiceImpl(
                itemRepository,
                userRepository,
                bookingRepository,
                commentRepository,
                itemRequestRepository
        );
    }

    @Test
    void create() {

        // приходит на контроллен:
        ItemDto itemDto = ItemDto.builder().name("Дрель").description("Простая дрель").available(true).build();
        Long ownerId = 1L;

        User userFromDb = User.builder().id(1L).name("User1").email("user1@user.com").build();
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userFromDb));

        Item itemFromDb = Item.builder().id(1L).name("Дрель").description("Простая дрель").available(true).owner(userFromDb).build();

        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(itemFromDb);

        assertEquals(itemService.create(itemDto, 1L), ItemMapper.getItemDto(itemFromDb));

        Mockito.verify(itemRepository, Mockito.times(1)).save(Mockito.any(Item.class));

    }

    @Test
    void findById() {
        User userFromDb = User.builder().id(1L).name("User1").email("user1@user.com").build();
        Item itemFromDb = Item.builder().id(1L).name("Дрель").description("Простая дрель").available(true).owner(userFromDb).build();

        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(itemFromDb));

        User booker = User.builder().id(2L).name("User2").email("user2@user.com").build();

        Booking booking1 = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(itemFromDb)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .text("text")
                .item(itemFromDb)
                .author(userFromDb)
                .created(LocalDateTime.now())
                .build();

        Mockito.when(bookingRepository.findAllByItem(itemFromDb)).thenReturn(List.of(booking1));
        Mockito.when(commentRepository.findAllByItemId(1L)).thenReturn(List.of(comment));

        ItemDto expectedItem = ItemDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Простая дрель")
                .ownerId(1L)
                .available(true)
                .comments(List.of(CommentMapper.getCommentDto(comment)))
                .lastBooking(ItemMapper.getItemBookingDto(booking1))
                .build();

        assertEquals(itemService.findById(1L, 1L), expectedItem);

    }

    @Test
    void findAll() {
    }

    @Test
    void update() {
    }

    @Test
    void search() {
    }

    @Test
    void createComment() {
    }
}