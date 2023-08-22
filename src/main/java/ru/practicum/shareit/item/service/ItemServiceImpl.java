package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

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
    public ItemDto findById(Long itemId, Long userId) {
        log.info("ItemServiceImpl: получение элемента по id: {}", itemId);
        Item itemFromDb = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Элемента с id: " + itemId + " не найден"));

        Optional<Booking> lastBooking = bookingRepository.findByItemAndItemOwnerIdAndEndBeforeOrderByStartDesc(itemFromDb, userId, LocalDateTime.now())
                .stream().filter(x -> x.getStatus().equals(BookingStatus.APPROVED)).findFirst();
        Optional<Booking> nextBooking = bookingRepository.findByItemAndItemOwnerIdAndStartAfterOrderByStartAsc(itemFromDb, userId, LocalDateTime.now())
                .stream().filter(x -> x.getStatus().equals(BookingStatus.APPROVED)).findFirst();
        ItemDto itemDto = ItemMapper.getItemDto(itemFromDb);
        if (nextBooking.isPresent() && lastBooking.isPresent()) {
            itemDto.setLastBooking(ItemMapper.getItemBookingDto(lastBooking.get()));
            itemDto.setNextBooking(ItemMapper.getItemBookingDto(nextBooking.get()));
        }
        return itemDto;
    }

    private ItemDto getBooking(Item item) {
        Long userId = item.getOwner().getId();
        ItemDto itemDto = ItemMapper.getItemDto(item);

        Optional<Booking> lastBooking = bookingRepository.findByItemAndItemOwnerIdAndEndBeforeOrderByStartDesc(item, userId, LocalDateTime.now())
                .stream().filter(x -> x.getStatus().equals(BookingStatus.APPROVED)).findFirst();
        Optional<Booking> nextBooking = bookingRepository.findByItemAndItemOwnerIdAndStartAfterOrderByStartAsc(item, userId, LocalDateTime.now())
                .stream().filter(x -> x.getStatus().equals(BookingStatus.APPROVED)).findFirst();
        if (nextBooking.isPresent() && lastBooking.isPresent()) {
            itemDto.setLastBooking(ItemMapper.getItemBookingDto(lastBooking.get()));
            itemDto.setNextBooking(ItemMapper.getItemBookingDto(nextBooking.get()));
        }
        return itemDto;
    }

    @Override
    public List<ItemDto> findAll(Long userId) {
        log.info("ItemServiceImpl: получение списка всех элементов для пользователя с id: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + userId + " не найден"));

        return itemRepository.findByOwner(user).stream().map(this::getBooking).collect(Collectors.toList());
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
