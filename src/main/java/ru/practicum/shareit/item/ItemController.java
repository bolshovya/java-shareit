package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("ItemController POST: сохранение элемента: {}, для пользователя в с id: {}", itemDto, userId);
        return itemService.create(itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("ItemController GET: получение элемента по id: {}", itemId);
        return itemService.findById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> findAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("ItemController GET: получение списка всех элементов для пользователя с id: {}", userId);
        return itemService.findAll(userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto, @PathVariable Long itemId,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("ItemController PATCH: обновление данных элемента с id: {}", itemId);
        return itemService.update(itemId, userId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("ItemController GET: поиск элементов содержащих: {} для пользователя с id: {}", text, userId);
        return itemService.search(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@Valid @RequestBody CommentDto commentDto, @PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("ItemController POST: сохранение комментария: {} для элемента. Item Id: {}, User Id: {}", commentDto, itemId, userId);
        return itemService.createComment(commentDto, itemId, userId);
    }
}
