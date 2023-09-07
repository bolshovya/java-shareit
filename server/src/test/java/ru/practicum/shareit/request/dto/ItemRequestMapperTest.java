package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperTest {

    @Test
    void getItemRequest() {

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("desc")
                .requestor(new UserDto())
                .created(LocalDateTime.now())
                .build();

        ItemRequest actual = ItemRequestMapper.getItemRequest(itemRequestDto);
    }

    @Test
    void getItemRequestDto() {

        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("desc")
                .requestor(new User())
                .created(LocalDateTime.now())
                .build();

        ItemRequestDto actual = ItemRequestMapper.getItemRequestDto(itemRequest);


    }
}