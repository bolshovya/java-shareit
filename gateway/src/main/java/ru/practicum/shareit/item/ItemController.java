package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import static ru.practicum.shareit.utils.Constant.USER_ID;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @Valid @RequestBody ItemDto itemDto,
            @RequestHeader(USER_ID) Long userId
    ) {
        log.info("Creating item: {}", itemDto);
        return itemClient.create(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(
            @PathVariable Long itemId,
            @RequestHeader(USER_ID) Long userId
    ) {
        log.info("Get item: {}", itemId);
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(
            @RequestHeader(USER_ID) Long userId
    ) {
        log.info("Get items");
        return itemClient.getItems(userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(
            @RequestBody ItemDto itemDto,
            @PathVariable Long itemId,
            @RequestHeader(USER_ID) Long userId
    ) {
        log.info("Update item: {}", itemDto);
        return itemClient.update(itemId, userId, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @RequestParam(name = "text") String text,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = "20") @Min(1) Integer size,
            @RequestHeader(USER_ID) Long userId
    ) {
        log.info("Search with text: {}", text);
        return itemClient.search(text, from, size, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(
            @Valid @RequestBody CommentDto commentDto,
            @PathVariable Long itemId,
            @RequestHeader(USER_ID) Long userId
    ) {
        log.info("Creating comment: {}", commentDto);
        return itemClient.createComment(commentDto, itemId, userId);
    }
}