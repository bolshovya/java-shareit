package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequestDto> findAllOfYour(Long userId, Integer from, Integer size);

    List<ItemRequestDto> findAllOfOther(Long userId, Integer from, Integer size);
}
