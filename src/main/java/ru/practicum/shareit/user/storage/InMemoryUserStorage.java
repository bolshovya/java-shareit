package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> storage;

    private Long userId;


    private Long implement() {
        return ++userId;
    }

    @Override
    public User create(User user) {
        log.info("InMemoryUserStorage: сохранение пользователя: {}", user);
        user.setId(implement());
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        log.info("InMemoryUserStorage: получение пользователя с id: {}", id);
        return Optional.of(storage.get(id));
    }

    @Override
    public List<User> findAll() {
        log.info("InMemoryUserStorage: получение списка всех пользователей");
        return (List<User>) storage.values();
    }

    @Override
    public User update(User user) {
        log.info("InMemoryUserStorage: обновление данных пользователя с id: {}", user.getId());
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(Long id) {
        log.info("InMemoryUserStorage: удаление пользователя с id: {}", id);
        storage.remove(id);
    }
}
