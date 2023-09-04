package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.dto.UserMapper;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequest getItemRequest(ItemRequestDto itemRequestDto) {
        return ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .requestor(UserMapper.getUser(itemRequestDto.getRequestor()))
                .created(itemRequestDto.getCreated())
                .build();
    }

    public static ItemRequestDto getItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(UserMapper.getUserDto(itemRequest.getRequestor()))
                .created(itemRequest.getCreated())
                .build();
    }
}
