package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.ItemValidationException;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        ItemDto itemDto = ItemDto.builder().name("Дрель").description("Простая дрель").requestId(1L).available(true).build();
        Long ownerId = 1L;

        User userFromDb = User.builder().id(1L).name("User1").email("user1@user.com").build();

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userFromDb));

        Mockito.when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(ItemRequest.builder().id(1L).build()));

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
        User owner = User.builder().id(1L).name("User1").email("user1@user.com").build();
        Item item1 = Item.builder().id(1L).name("Дрель").description("Простая дрель").available(true).owner(owner).build();
        Item item2 = Item.builder().id(2L).name("Шуруповерт").description("Простой шуруповерт").available(true).owner(owner).build();

        Mockito.when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        Mockito.when(itemRepository.findByOwner(owner)).thenReturn(List.of(item1, item2));

        ItemDto itemDto1 = ItemMapper.getItemDto(item1);
        itemDto1.setComments(new ArrayList<>());
        ItemDto itemDto2 = ItemMapper.getItemDto(item2);
        itemDto2.setComments(new ArrayList<>());

        assertEquals(itemService.findAll(owner.getId()), List.of(itemDto1, itemDto2));
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(itemRepository, Mockito.times(1)).findByOwner(Mockito.any(User.class));
    }

    @Test
    void updateDescription() {
        User owner = User.builder().id(1L).name("User1").email("user1@user.com").build();
        Item item1 = Item.builder().id(1L).name("Дрель").description("Простая дрель").available(true).owner(owner).build();
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item1));
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(owner));

        ItemDto itemUpdate = ItemDto.builder().id(1L).description("Обновленная дрель").build();
        Item expectedItem = Item.builder().id(1L).name("Дрель").description("Обновленная дрель").available(true).owner(owner).build();
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(expectedItem);

        assertEquals(itemService.update(1L, 1L, itemUpdate), ItemMapper.getItemDto(expectedItem));

        Mockito.verify(itemRepository, Mockito.times(1)).save(Mockito.any(Item.class));
    }

    @Test
    void updateName() {
        User owner = User.builder().id(1L).name("User1").email("user1@user.com").build();
        Item item1 = Item.builder().id(1L).name("Дрель").description("Простая дрель").available(true).owner(owner).build();
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item1));
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(owner));

        ItemDto itemUpdate = ItemDto.builder().id(1L).name("Обновленная дрель").build();
        Item expectedItem = Item.builder().id(1L).name("Обновленная дрель").description("Простая дрель").available(true).owner(owner).build();
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(expectedItem);

        assertEquals(itemService.update(1L, 1L, itemUpdate), ItemMapper.getItemDto(expectedItem));

        Mockito.verify(itemRepository, Mockito.times(1)).save(Mockito.any(Item.class));
    }

    @Test
    void search() {
        User owner = User.builder().id(1L).name("User1").email("user1@user.com").build();
        Item item1 = Item.builder().id(1L).name("Дрель").description("Простая дрель").available(true).owner(owner).build();
        String text = "простая";
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(owner));

        Mockito.when(itemRepository.findByNameOrDescriptionContaining(text)).thenReturn(List.of(item1));

        assertEquals(itemService.search(text, owner.getId()), List.of(ItemMapper.getItemDto(item1)));

        Mockito.verify(itemRepository, Mockito.times(1)).findByNameOrDescriptionContaining(text);
    }

    @Test
    void searchEmptyList() {
        User owner = User.builder().id(1L).name("User1").email("user1@user.com").build();
        String text = "";

        assertEquals(itemService.search(text, owner.getId()), new ArrayList<>());
    }

    @Test
    void createComment() {
        LocalDateTime time = LocalDateTime.now();
        User author = User.builder().id(1L).name("User1").email("user1@user.com").build();
        Item item1 = Item.builder().id(1L).name("Дрель").description("Простая дрель").available(true).owner(author).build();
        CommentDto commentFromController = CommentDto.builder().text("text").build();
        Comment comment = Comment.builder().id(1L).text("text").item(item1).author(author).created(time).build();
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item1));
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(author));
        Mockito.when(bookingRepository.findAllByBookerAndStatusAndStartBefore(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of(new Booking()));
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);
        CommentDto expectedComment = CommentDto.builder().id(1L).text("text").authorName("User1").created(time).build();

        assertEquals(itemService.createComment(commentFromController, 1L, 1L), expectedComment);
        Mockito.verify(commentRepository, Mockito.times(1)).save(Mockito.any(Comment.class));
    }

    @Test
    void createCommentshouldReturnExpection() {
        LocalDateTime time = LocalDateTime.now();
        User author = User.builder().id(1L).name("User1").email("user1@user.com").build();
        Item item1 = Item.builder().id(1L).name("Дрель").description("Простая дрель").available(true).owner(author).build();
        CommentDto commentFromController = CommentDto.builder().text("text").build();
        Comment comment = Comment.builder().id(1L).text("text").item(item1).author(author).created(time).build();
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item1));
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(author));
        Mockito.when(bookingRepository.findAllByBookerAndStatusAndStartBefore(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>());
        //Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);
        //CommentDto expectedComment = CommentDto.builder().id(1L).text("text").authorName("User1").created(time).build();
        assertThrows(ItemValidationException.class, () -> itemService.createComment(commentFromController, 1L, 1L));
        //assertEquals(itemService.createComment(commentFromController, 1L, 1L), expectedComment);
        //Mockito.verify(commentRepository, Mockito.times(1)).save(Mockito.any(Comment.class));
    }
}