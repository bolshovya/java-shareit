package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @Valid @RequestBody UserDto userDto
    ) {
        log.info("Creating user {}", userDto);
        return userClient.create(userDto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(
            @PathVariable Long id
    ) {
        log.info("Get user: {}", id);
        return userClient.getUser(id);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(
    ) {
        log.info("Get users");
        return userClient.getUsers();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(
            @RequestBody UserDto userDto,
            @PathVariable Long userId
    ) {
        log.info("Update user: {}", userDto);
        return userClient.update(userId, userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(
            @PathVariable Long id
    ) {
        log.info("Delete user: {}", id);
        userClient.delete(id);
    }

}