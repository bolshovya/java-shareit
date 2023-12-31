package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(
            @RequestBody UserDto userDto
    ) {
        log.info("UserController POST: сохранение пользователя: {}", userDto);
        return userService.create(userDto);
    }

    @GetMapping("/{id}")
    public UserDto findById(
            @PathVariable Long id
    ) {
        log.info("UserController GET: получение пользователя с id: {}", id);
        return userService.findById(id);
    }

    @GetMapping
    public List<UserDto> findAll(

    ) {
        log.info("UserController GET: получение списка всех пользователей");
        return userService.findAll();
    }

    @PatchMapping("/{userId}")
    public UserDto update(
            @RequestBody UserDto userDto,
            @PathVariable Long userId
    ) {
        log.info("UserController PATCH: обновление данных пользователя с id: {}", userId);
        userDto.setId(userId);
        return userService.update(userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(
            @PathVariable Long id
    ) {
        log.info("UserController DELETE: удаление пользователя с id: {}", id);
        userService.delete(id);
    }

}