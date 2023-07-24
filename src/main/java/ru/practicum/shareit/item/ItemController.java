package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        if (userId != null) {
            itemDto.setUserId(userId);
        }
        log.info("ItemController POST: сохранение элемента: {}", itemDto);
        return itemService.create(itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable Long itemId) {
        log.info("ItemController GET: получение элемента по id: {}", itemId);
        return itemService.findById(itemId);
    }

    @GetMapping
    public List<ItemDto> findAll() {
        log.info("ItemController GET: получение списка всех элементов");
        return itemService.findAll();
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        log.info("ItemController PATCH: обновление данных элемента с id: {}", itemId);
        itemService.findById(itemId);
        return itemService.update(itemId, itemDto);
    }


}
