package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        log.info("ItemServiceImpl: сохранение элемента: {}, для пользователя в с id: {}", itemDto, userId);
        userStorage.findById(userId).orElseThrow(() -> new UserNotFoundException());
        itemDto.setUserId(userId);
        Item createdItem = itemStorage.create(ItemMapper.getItem(itemDto));
        return ItemMapper.getItemDto(createdItem);
    }

    @Override
    public ItemDto findById(Long itemId) {
        log.info("ItemServiceImpl: получение элемента по id: {}", itemId);
        Item itemFromDb = itemStorage.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Элемента с id: " + itemId + " не найден"));
        return ItemMapper.getItemDto(itemFromDb);
    }

    @Override
    public List<ItemDto> findAll(Long userId) {
        log.info("ItemServiceImpl: получение списка всех элементов для пользователя с id: {}", userId);
        return itemStorage.findAll().stream().filter(x -> x.getUserId() == userId).map(ItemMapper::getItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto update(Long itemId, Long userId, ItemDto itemDtoUpdate) {
        log.info("ItemServiceImpl: обновление данных элемента с id: {}", itemId);
        ItemDto itemFromDb = findById(itemId);
        userStorage.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + userId + " не найден"));
        if (itemFromDb.getUserId() != userId) {
            throw new ItemNotFoundException("id пользователей не совпадают");
        }
        itemDtoUpdate.setUserId(userId);
        return ItemMapper.getItemDto(itemStorage.update(itemId, ItemMapper.getItem(itemDtoUpdate)));
    }

    @Override
    public List<ItemDto> search(String text, Long userId) {
        log.info("ItemServiceImpl: поиск элементов содержащих: {}", text);
        return itemStorage.search(text, userId).stream().map(ItemMapper::getItemDto).collect(Collectors.toList());
    }



}
