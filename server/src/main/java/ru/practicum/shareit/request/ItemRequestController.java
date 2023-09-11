package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static ru.practicum.shareit.utils.Constant.USER_ID;

@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(
            @RequestBody ItemRequestDto itemRequestDto,
            @RequestHeader(USER_ID) Long userId
    ) {
        log.info("ItemRequestController POST: сохранение запроса: {}, для пользователя с id: {}", itemRequestDto, userId);
        return itemRequestService.create(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> findAllOfYour(
            @RequestHeader(USER_ID) Long userId,
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        log.info("ItemRequestController GET: получение списка всех запросов для пользователя с id: {}", userId);
        return itemRequestService.findAllOfYour(userId, from, size);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllOfOther(
            @RequestHeader(USER_ID) Long userId,
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        log.info("ItemRequestController GET: получение списка всех запросов для пользователя с id: {}", userId);
        return itemRequestService.findAllOfOther(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findById(
            @PathVariable Long requestId,
            @RequestHeader(USER_ID) Long userId
    ) {
        log.info("ItemRequestController GET: получение запроса с id: {}, для пользователя с id:", requestId, userId);
        return itemRequestService.findById(requestId, userId);
    }
}