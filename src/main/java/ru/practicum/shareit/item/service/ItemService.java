package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto);

    ItemDto findById(Long id);

    List<ItemDto> findAll();

    ItemDto update(Long itemId, ItemDto itemUpdate);
}
