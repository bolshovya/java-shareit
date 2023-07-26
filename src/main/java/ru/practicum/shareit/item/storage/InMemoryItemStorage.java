package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Long, Item> storage;

    private long itemId;

    private Long implement() {
        return ++itemId;
    }

    @Override
    public Item create(Item item) {
        log.info("InMemoryItemStorage: сохранение элемента: {}", item);
        item.setId(implement());
        log.info("InMemoryItemStorage: сохраняемому элементу присвоен id: {}", item.getId());
        storage.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> findById(Long itemId) {
        log.info("InMemoryItemStorage: получение элемента по id: {}", itemId);
        return Optional.of(storage.get(itemId));
    }

    @Override
    public List<Item> findAll() {
        log.info("InMemoryItemStorage: получение списка всех элементов");
        return (List<Item>) storage.values();
    }

    @Override
    public Item update(Long itemId, Item itemUpdate) {
        log.info("InMemoryItemStorage: обновление данных элемента с id: {}", itemId);
        Item itemFromDb = storage.get(itemId);
        itemUpdate.setId(itemId);
        if (itemUpdate.getName() == null) {
            itemUpdate.setName(itemFromDb.getName());
        }
        if (itemUpdate.getDescription() == null) {
            itemUpdate.setDescription(itemFromDb.getDescription());
        }
        if (itemUpdate.getAvailable() == null) {
            itemUpdate.setAvailable(itemFromDb.getAvailable());
        }
        storage.put(itemId, itemUpdate);
        return itemUpdate;
    }



}
