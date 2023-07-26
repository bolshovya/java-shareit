package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.UserValidationException;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
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
        checkEmailValid(newUser);
        checkEmailExists(newUser);
        log.info("InMemoryUserStorage: сохранение пользователя: {}", newUser);
        newUser.setId(implement());
        log.info("InMemoryUserStorage: сохраняемому пользователю присвоен id: {}", newUser.getId());
        storage.put(newUser.getId(), newUser);
        return newUser;
    }

    private static void checkEmailValid(User newUser) {
        if (newUser.getEmail() == null) {
            throw new UserValidationException("Email не может быть пустым");
        }
        if (!newUser.getEmail().contains("@")) {
            throw new UserValidationException("Email должен содержать @");
        }
    }

    private void checkEmailExists(User newUser) {
        for (User user : storage.values()) {
            if (user.getEmail().equals(newUser.getEmail()) && !user.getId().equals(newUser.getId())) {
                throw new ConflictException("Пользователь с email: " + newUser.getEmail() + " уже есть в базе данных");
            }
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        log.info("InMemoryUserStorage: получение пользователя с id: {}", id);
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<User> findAll() {
        log.info("InMemoryUserStorage: получение списка всех пользователей");
        return new ArrayList<>(storage.values());
    }

    @Override
    public User updatePut(User user) {
        log.info("InMemoryUserStorage: обновление данных пользователя с id: {}", user.getId());
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User userUpdate) {
        log.info("InMemoryUserStorage: обновление данных пользователя: {}", userUpdate);
        checkEmailExists(userUpdate);
        User userFromDb = storage.get(userUpdate.getId());
        if (userUpdate.getName() == null) {
            userUpdate.setName(userFromDb.getName());
        }
        if (userUpdate.getEmail() == null) {
            userUpdate.setEmail(userFromDb.getEmail());
        }
        storage.put(userUpdate.getId(), userUpdate);
        return userUpdate;
    }

    @Override
    public void delete(Long id) {
        log.info("InMemoryUserStorage: удаление пользователя с id: {}", id);
        storage.remove(id);
    }
}
