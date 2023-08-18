package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        log.info("ItemServiceImpl: сохранение элемента: {}, для пользователя в с id: {}", itemDto, userId);
        User userFromDb = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Item itemToDb = ItemMapper.getItem(itemDto);
        itemToDb.setOwner(userFromDb);
        Item createdItem = itemRepository.save(itemToDb);
        log.info("ItemServiceImpl: сохранен элемент: {}", itemDto);
        return ItemMapper.getItemDto(createdItem);
    }

    @Override
    public ItemDto findById(Long itemId) {
        log.info("ItemServiceImpl: получение элемента по id: {}", itemId);
        Item itemFromDb = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Элемента с id: " + itemId + " не найден"));
        return ItemMapper.getItemDto(itemFromDb);
    }

    @Override
    public List<ItemDto> findAll(Long userId) {
        log.info("ItemServiceImpl: получение списка всех элементов для пользователя с id: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + userId + " не найден"));
        return itemRepository.findByOwner(user).stream().map(ItemMapper::getItemDto).collect(Collectors.toList());
        //return itemRepository.findByOwner(userId).stream().map(ItemMapper::getItemDto).collect(Collectors.toList());
        // return itemStorage.findAll().stream().filter(x -> x.getUserId() == userId).map(ItemMapper::getItemDto).collect(Collectors.toList());
    }


    @Override
    public ItemDto update(Long itemId, Long userId, ItemDto itemDtoUpdate) {
        log.info("ItemServiceImpl: обновление данных элемента с id: {}", itemId);

        // ItemDto itemFromDb = findById(itemId);

        Item itemFromDb = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Элемент с id: " + itemId + " не найден"));

        User userFromDb = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + userId + " не найден"));

        if (itemDtoUpdate.getName() == null) {
            itemDtoUpdate.setName(itemFromDb.getName());
        }
        if (itemDtoUpdate.getDescription() == null) {
            itemDtoUpdate.setDescription(itemFromDb.getDescription());
        }
        if (itemDtoUpdate.getAvailable() == null) {
            itemDtoUpdate.setAvailable(itemFromDb.getAvailable());
        }

        Item itemToDb = ItemMapper.getItem(itemDtoUpdate);
        itemToDb.setId(itemId);
        itemToDb.setOwner(userFromDb);


        /*
        if (itemToDb.getName() == null) {
            itemToDb.setName(itemFromDb.getName());
        }
        if (itemToDb.getDescription() == null) {
            itemToDb.setDescription(itemFromDb.getDescription());
        }
        if (itemToDb.getAvailable() == null) {
            itemToDb.setAvailable(itemFromDb.getAvailable());
        }

         */
        /*
        if (!itemFromDb.getOwner().getId().equals(userId)) {
            throw new ItemNotFoundException("id пользователей не совпадают");
        }

         */
        //itemDtoUpdate.setOwner(userFromDb);
        return ItemMapper.getItemDto(itemRepository.save(itemToDb));
    }


    @Override
    public List<ItemDto> search(String text, Long userId) {
        log.info("ItemServiceImpl: поиск элементов содержащих: {}", text);
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + userId + " не найден"));
        return itemRepository.findByNameOrDescriptionContaining(text).stream().map(ItemMapper::getItemDto).collect(Collectors.toList());
    }

}
