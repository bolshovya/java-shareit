package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User create(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    User updatePut(User user);

    User update(User userUpdate);

    void delete(Long id);
}
