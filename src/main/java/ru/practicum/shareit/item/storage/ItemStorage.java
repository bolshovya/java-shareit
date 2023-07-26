package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Item create(Item item);

    Optional<Item> findById(Long id);

    List<Item> findAll();

    Item update(Long itemId, Item itemUpdate);

    List<Item> search(String text, Long userId);
}
