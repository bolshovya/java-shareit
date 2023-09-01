package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

class ItemRequestServiceTest {

    private ItemRequestService itemRequestService;

    private ItemRequestRepository itemRequestRepository;

    private UserRepository userRepository;

    private ItemRepository itemRepository;

    private User user1;
    private User user2;
    private Item item1;

    private ItemRequestDto itemRequestDto;

    private ItemRequest itemRequest;

    @BeforeEach
    void init() {
        LocalDateTime start = LocalDateTime.now();
        itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userRepository, itemRepository);
        user1 = User.builder().id(1L).name("User1").email("user1@user.com").build();
        user2 = User.builder().id(2L).name("User2").email("user2@user.com").build();
        item1 = Item.builder().id(1L).name("Дрель").description("Простая дрель").available(true).owner(user1).build();
        itemRequestDto = ItemRequestDto.builder().description("Хотел бы воспользоваться щёткой для обуви").build();
        itemRequest = ItemRequest.builder()
                .id(1L).description("Хотел бы воспользоваться щёткой для обуви").requestor(user2).created(start).build();
    }

    @Test
    void create() {
        Mockito.when(
                userRepository.
                        findById(Mockito.anyLong()))
                        .thenReturn(Optional.of(user2));

        Mockito.when(
                itemRequestRepository
                        .save(Mockito.any()))
                        .thenReturn(itemRequest);

        itemRequestService.create(itemRequestDto, 2L);

        Mockito.verify(
                itemRequestRepository,
                Mockito.times(1))
                .save(Mockito.any());
    }

    @Test
    void findAllOfYour() {
        Mockito.when(
                userRepository
                        .findById(Mockito.anyLong()))
                        .thenReturn(Optional.of(user2));

        Mockito.when(
                itemRequestRepository
                        .findAllByRequestor(Mockito.any(), Mockito.any()))
                        .thenReturn(List.of(itemRequest));

        itemRequestService.findAllOfYour(1L, 1, 1);

        Mockito.verify(
                itemRequestRepository,
                Mockito.times(1))
                .findAllByRequestor(Mockito.any(), Mockito.any());
    }

    @Test
    void findAllOfOther() {
        Mockito.when(
                userRepository
                        .findById(Mockito.anyLong()))
                        .thenReturn(Optional.of(user2));

        Mockito.when(
                itemRequestRepository
                        .findAllByRequestorNot(Mockito.any(), Mockito.any()))
                        .thenReturn(List.of(itemRequest));

        itemRequestService.findAllOfOther(1L, 1, 1);

        Mockito.verify(
                itemRequestRepository,
                Mockito.times(1))
                .findAllByRequestorNot(Mockito.any(), Mockito.any());
    }

    @Test
    void findById() {
        Mockito.when(
                userRepository
                        .findById(Mockito.anyLong()))
                        .thenReturn(Optional.of(user2));

        Mockito.when(
                itemRequestRepository
                        .findById(Mockito.anyLong()))
                        .thenReturn(Optional.of(itemRequest));

        itemRequestService.findById(1L, 1L);

        Mockito.verify(
                itemRequestRepository,
                Mockito.times(1))
                .findById(1L);
    }
}