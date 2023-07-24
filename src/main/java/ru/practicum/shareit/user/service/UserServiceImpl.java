package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public UserDto create(UserDto userDto) {
        log.info("UserStorageImpl: сохранение пользователя: ", userDto);
        return UserMapper.getUserDto(userStorage.create(UserMapper.getUser(userDto)));
    }

    @Override
    public UserDto findById(Long id) {
        log.info("UserServiceImpl: получение пользователя по id: {}", id);
        User userFromDB = userStorage.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + id + " не найден"));
        return UserMapper.getUserDto(userFromDB);
    }

    @Override
    public List<UserDto> findAll() {
        log.info("UserServiceImpl: получение списка всех пользователей");
        return userStorage.findAll().stream().map(UserMapper::getUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto update(UserDto userDto) {
        findById(userDto.getId());
        log.info("UserServiceImpl: обновление пользователя с id: {}", userDto.getId());
        return UserMapper.getUserDto(userStorage.update(UserMapper.getUser(userDto)));
    }

    @Override
    public void delete(Long id) {
        findById(id);
        log.info("UserServiceImpl: удаление пользователя с id: {}", id);
        userStorage.delete(id);
    }
}
