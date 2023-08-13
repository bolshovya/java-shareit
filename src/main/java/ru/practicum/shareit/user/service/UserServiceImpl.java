package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserValidationException;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    @Override
    public UserDto create(UserDto userDto) {
        log.info("UserServiceImpl: сохранение пользователя: ", userDto);
        User createdUser = userRepository.save(UserMapper.getUser(userDto));
        return UserMapper.getUserDto(createdUser);
    }

    private void checkEmailValid(UserDto newUser) {
        if (newUser.getEmail() == null) {
            throw new UserValidationException("Email не может быть пустым");
        }
        if (!newUser.getEmail().contains("@")) {
            throw new UserValidationException("Email должен содержать @");
        }
    }

    private void checkEmailExists(UserDto newUser) {
        User userFromDb = userRepository.findByEmail(newUser.getEmail());
        if (userFromDb.getEmail().equals(newUser.getEmail())) {
            throw new ConflictException("Пользователь с email: " + newUser.getEmail() + " уже есть в базе данных");
        }
    }

    @Override
    public UserDto findById(Long id) {
        log.info("UserServiceImpl: получение пользователя по id: {}", id);
        User userFromDb = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + id + " не найден"));
        log.info("USerServiceImpl: пользователь с id: {} найден: {}", id, userFromDb);
        return UserMapper.getUserDto(userFromDb);
    }

    @Override
    public List<UserDto> findAll() {
        log.info("UserServiceImpl: получение списка всех пользователей");
        return userRepository.findAll().stream().map(UserMapper::getUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto update(UserDto userDto) {
        findById(userDto.getId());
        log.info("UserServiceImpl: обновление данных пользователя: {}", userDto);
        User updatedUser = userRepository.save(UserMapper.getUser(userDto));
        log.info("UserServiceImpl: обновлены данные пользователя {}", updatedUser);
        return UserMapper.getUserDto(updatedUser);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        log.info("UserServiceImpl: удаление пользователя с id: {}", id);
        userRepository.deleteById(id);
        itemRepository.deleteByOwner(id);
    }
}
