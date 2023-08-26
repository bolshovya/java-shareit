package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.storage.ItemRequestRepository;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    /**
     * POST /requests — добавить новый запрос вещи.
     * Основная часть запроса — текст запроса, где пользователь описывает, какая именно вещь ему нужна.
     */
    @PostMapping
    public ItemRequestDto create(@Valid @RequestBody ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("ItemRequestController POST: сохранение запроса: {}, для пользователя с id: {}", itemRequestDto, userId);
        return itemRequestService.create(itemRequestDto, userId);
    }

    /**
     * GET /requests — получить список своих запросов вместе с данными об ответах на них.
     * Для каждого запроса должны указываться описание, дата и время создания и список ответов в формате:
     * id вещи, название, её описание description, а также requestId запроса и признак доступности вещи available.
     * Так в дальнейшем, используя указанные id вещей, можно будет получить подробную информацию о каждой вещи.
     * Запросы должны возвращаться в отсортированном порядке от более новых к более старым.
     */
    @GetMapping
    public List<ItemRequestDto> findAllOfYour(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(value = "from", defaultValue = "0", required = false) @Min(0) Integer from,
                                              @RequestParam(value = "size", defaultValue = "1", required = false) @Min(1) Integer size) {
        log.info("ItemRequestController GET: получение списка всех запросов для пользователя с id: {}", userId);
        return itemRequestService.findAllOfYour(userId, from, size);
    }

    /**
     * GET /requests/all?from={from}&size={size} — получить список запросов, созданных другими пользователями.
     * С помощью этого эндпоинта пользователи смогут просматривать существующие запросы, на которые они могли бы ответить.
     * Запросы сортируются по дате создания: от более новых к более старым.
     * Результаты должны возвращаться постранично.
     * Для этого нужно передать два параметра: from — индекс первого элемента, начиная с 0, и size — количество элементов для отображения.
     */
    @GetMapping("/all")
    public List<ItemRequestDto> findAllOfOther(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(value = "from", defaultValue = "0", required = false) @Min(0) Integer from,
                                               @RequestParam(value = "size", defaultValue = "1", required = false) @Min(1) Integer size) {
        log.info("ItemRequestController GET: получение списка всех запросов для пользователя с id: {}", userId);
        return itemRequestService.findAllOfOther(userId, from, size);
    }

    /**
     * GET /requests/{requestId} — получить данные об одном конкретном запросе вместе с данными об ответах на него
     * в том же формате, что и в эндпоинте GET /requests.
     * Посмотреть данные об отдельном запросе может любой пользователь.
     */
    //@GetMapping
    public ItemRequestDto findById() {
        return null;
    }
}
