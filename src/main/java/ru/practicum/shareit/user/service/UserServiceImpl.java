package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        log.info("UserServiceImpl: сохранение пользователя: ", userDto);
        try {
            User createdUser = userRepository.save(UserMapper.getUser(userDto));
            return UserMapper.getUserDto(createdUser);
        } catch (ConstraintViolationException e) {
            throw new UserValidationException();
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

    @Transactional
    @Override
    public UserDto update(UserDto userDto) {
        UserDto userFromDb = findById(userDto.getId());
        if (userDto.getEmail() == null) {
            userDto.setEmail(userFromDb.getEmail());
        }
        if (userDto.getName() == null) {
            userDto.setName(userFromDb.getName());
        }
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
        // itemRepository.deleteByOwner(id);
    }
}
