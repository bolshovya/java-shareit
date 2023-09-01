package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    @Override
    public ItemRequestDto create(ItemRequestDto itemRequestDto, Long userId) {
        log.info("ItemRequestServiceImpl: сохранение запроса: {}, для пользователя с id: {}", itemRequestDto, userId);
        itemRequestDto.setCreated(LocalDateTime.now());
        User userFromDb = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        itemRequestDto.setRequestor(UserMapper.getUserDto(userFromDb));
        ItemRequest itemRequestFromDb = itemRequestRepository.save(ItemRequestMapper.getItemRequest(itemRequestDto));
        return ItemRequestMapper.getItemRequestDto(itemRequestFromDb);
    }

    @Override
    public List<ItemRequestDto> findAllOfYour(Long userId, Integer from, Integer size) {
        log.info("ItemRequestServiceImpl: получение списка всех запросов для пользователя с id: {}", userId);
        User userFromDb = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        return itemRequestRepository.findAllByRequestor(userFromDb, PageRequest.of(from/size, size, Sort.by("created").ascending()))
                .stream()
                .map(ItemRequestMapper::getItemRequestDto)
                .map(this::addItem)
                .collect(Collectors.toList());
    }

    private ItemRequestDto addItem(ItemRequestDto itemRequestDto) {
        Long id = itemRequestDto.getId();
        List<Item> items = itemRepository.findAllByRequestId(id);
        if (items.isEmpty()) {
            itemRequestDto.setItems(new ArrayList<>());
        }
        itemRequestDto.setItems(items.stream().map(ItemMapper::getItemRequestResponseDto).collect(Collectors.toList()));
        return itemRequestDto;
    }

    @Override
    public List<ItemRequestDto> findAllOfOther(Long userId, Integer from, Integer size) {
        log.info("ItemRequestServiceImpl: получение списка всех запросов для пользователя с id: {}", userId);
        User userFromDb = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        return itemRequestRepository.findAllByRequestorNot(userFromDb, PageRequest.of(from/size, size, Sort.by("created").ascending()))
                .stream()
                .map(ItemRequestMapper::getItemRequestDto)
                .map(this::addItem)
                .collect(Collectors.toList());

    }

    @Override
    public ItemRequestDto findById(Long requestId, Long userId) {
        log.info("ItemRequestServiceImpl: получение запроса с id: {}", requestId);
        User userFromDb = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        ItemRequest itemRequestFromDb = itemRequestRepository.findById(requestId)
                .orElseThrow(ItemNotFoundException::new);
        ItemRequestDto itemRequestDto = ItemRequestMapper.getItemRequestDto(itemRequestFromDb);
        addItem(itemRequestDto);
        return itemRequestDto;
    }
}
