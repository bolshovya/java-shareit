package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import static ru.practicum.shareit.utils.Constant.USER_ID;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestBody @Valid ItemRequestDto itemRequestDto,
            @RequestHeader(USER_ID) Long userId
    ) {
        log.info("Creating request: {}", itemRequestDto);
        return itemRequestClient.create(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllOfYour(
            @RequestHeader(USER_ID) Long userId,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size
    ) {
        log.info("Get requests for user: {}", userId);
        return itemRequestClient.findAllOfYour(userId, from, size);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllOfOther(
            @RequestHeader(USER_ID) Long userId,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size
    ) {
        log.info("Get requests for user: {}", userId);
        return itemRequestClient.findAllOfOther(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(
            @PathVariable Long requestId,
            @RequestHeader(USER_ID) Long userId
    ) {
        log.info("Get request: {}", requestId);
        return itemRequestClient.getItemRequest(requestId, userId);
    }

}