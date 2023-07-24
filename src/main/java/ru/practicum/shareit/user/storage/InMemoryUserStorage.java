package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> storage;

    private long userId;


    private Long implement() {
        return ++userId;
    }

    @Override
    public User create(User newUser) {
        for (User user : storage.values()) {
            if (user.getEmail().equals(newUser.getEmail())) {
                throw new RuntimeException("Пользователь с email: " + newUser.getEmail() + " уже есть в базе данных");
            }
        }
        newUser.setId(implement());
        log.info("InMemoryUserStorage: сохранение пользователя: {}", newUser);
        storage.put(newUser.getId(), newUser);
        return newUser;
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
