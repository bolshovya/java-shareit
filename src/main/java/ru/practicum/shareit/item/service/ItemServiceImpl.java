package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto create(ItemDto itemDto) {
        log.info("ItemServiceImpl: сохранение элемента: {}", itemDto);
        userStorage.findById(itemDto.getUserId()).orElseThrow(() -> new UserNotFoundException());
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
    public List<ItemDto> findAll() {
        log.info("ItemServiceImpl: получение списка всех элементов");
        return itemStorage.findAll().stream().map(ItemMapper::getItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto update(Long itemId, ItemDto itemUpdate) {
        log.info("ItemServiceImpl: обновление данных элемента с id: {}", itemId);
        itemStorage.findById(itemId);
        return ItemMapper.getItemDto(itemStorage.update(itemId, ItemMapper.getItem(itemUpdate)));
    }



}
