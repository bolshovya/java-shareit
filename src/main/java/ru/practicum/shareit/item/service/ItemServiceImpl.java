package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ItemValidationException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        log.info("ItemServiceImpl: сохранение элемента: {}, для пользователя в с id: {}", itemDto, userId);
        User userFromDb = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Item itemToDb = ItemMapper.getItem(itemDto);
        itemToDb.setOwner(userFromDb);
        Item createdItem = itemRepository.save(itemToDb);
        log.info("ItemServiceImpl: сохранен элемент: {}", itemDto);
        return ItemMapper.getItemDto(createdItem);
    }

    @Transactional
    @Override
    public ItemDto findById(Long itemId, Long userId) {
        log.info("ItemServiceImpl: получение элемента по id: {}", itemId);
        Item itemFromDb = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Элемента с id: " + itemId + " не найден"));

        ItemDto itemDto = ItemMapper.getItemDto(itemFromDb);

        Optional<Booking> lastBooking = bookingRepository.findAllByItem(itemFromDb)
                .stream()
                .filter(x -> x.getItem().getOwner().getId().equals(userId))
                .filter(x -> x.getStart().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(Booking::getEnd));

        Optional<Booking> nextBooking = bookingRepository.findAllByItem(itemFromDb)
                .stream()
                .filter(x -> x.getItem().getOwner().getId().equals(userId))
                .filter(x -> x.getStatus().equals(BookingStatus.APPROVED))
                .filter(x -> x.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStart));

        lastBooking.ifPresent(booking -> itemDto.setLastBooking(ItemMapper.getItemBookingDto(booking)));

        nextBooking.ifPresent(booking -> itemDto.setNextBooking(ItemMapper.getItemBookingDto(booking)));

        itemDto.setComments(setComment(itemId));
        return itemDto;
    }

    private List<CommentDto> setComment(Long itemId) {
        return commentRepository.findAllByItemId(itemId)
                .stream()
                .map(CommentMapper::getCommentDto)
                .collect(Collectors.toList());
    }

    private ItemDto addComment(ItemDto itemDto) {
        itemDto.setComments(commentRepository.findAllByItemId(itemDto.getId())
                .stream()
                .map(CommentMapper::getCommentDto)
                .collect(Collectors.toList()));
        return itemDto;
    }

    private ItemDto addBooking(Item item) {
        Long userId = item.getOwner().getId();
        ItemDto itemDto = ItemMapper.getItemDto(item);

        Optional<Booking> lastBooking = bookingRepository.findAllByItem(item)
                .stream()
                .filter(x -> x.getItem().getOwner().getId().equals(userId))
                .filter(x -> x.getStart().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(Booking::getEnd));

        Optional<Booking> nextBooking = bookingRepository.findAllByItem(item)
                .stream()
                .filter(x -> x.getItem().getOwner().getId().equals(userId))
                .filter(x -> x.getStatus().equals(BookingStatus.APPROVED))
                .filter(x -> x.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStart));

        lastBooking.ifPresent(booking -> itemDto.setLastBooking(ItemMapper.getItemBookingDto(booking)));

        nextBooking.ifPresent(booking -> itemDto.setNextBooking(ItemMapper.getItemBookingDto(booking)));
        return itemDto;
    }

    @Transactional
    @Override
    public List<ItemDto> findAll(Long userId) {
        log.info("ItemServiceImpl: получение списка всех элементов для пользователя с id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + userId + " не найден"));

        return itemRepository.findByOwner(user)
                .stream()
                .map(this::addBooking)
                .map(this::addComment)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ItemDto update(Long itemId, Long userId, ItemDto itemDtoUpdate) {
        log.info("ItemServiceImpl: обновление данных элемента с id: {}", itemId);

        Item itemFromDb = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Элемент с id: " + itemId + " не найден"));

        User userFromDb = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + userId + " не найден"));

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

        return ItemMapper.getItemDto(itemRepository.save(itemToDb));
    }

    @Override
    public List<ItemDto> search(String text, Long userId) {
        log.info("ItemServiceImpl: поиск элементов содержащих: {}", text);

        if (text.isEmpty()) {
            return new ArrayList<>();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + userId + " не найден"));

        return itemRepository.findByNameOrDescriptionContaining(text)
                .stream()
                .map(ItemMapper::getItemDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDto createComment(CommentDto commentDto, Long itemId, Long userId) {
        log.info("ItemServiceImpl: сохранение комментария: {} для элемента. Item Id: {}, User Id: {}", commentDto, itemId, userId);
        commentDto.setCreated(LocalDateTime.now());
        Comment commentToDb = CommentMapper.getComment(commentDto);
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + userId + " не найден"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Элемент с id: " + itemId + " не найден"));

        if (bookingRepository.findAllByBookerAndStatusAndStartBefore(author, BookingStatus.APPROVED, LocalDateTime.now()).isEmpty()) {
            throw new ItemValidationException("Пользователь с id: " + userId + " не брал в аренду элемент с id: " + itemId);
        }

        commentToDb.setAuthor(author);
        commentToDb.setItem(item);
        return CommentMapper.getCommentDto(commentRepository.save(commentToDb));
    }
}
