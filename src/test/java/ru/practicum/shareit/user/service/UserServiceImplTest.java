package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserServiceImplTest {

    private UserServiceImpl userService;

    private UserRepository userRepository;

    @BeforeEach
    void init() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testUserCreate() {
        // ожидаю получить на выходе
        UserDto expectedUserDto = UserDto.builder().id(1L).name("User1").email("user1@user.com").build();

        // приходит на контроллер
        UserDto userDtoRequest = UserDto.builder().name("User1").email("user1@user.com").build();

        // получаем с БД
        User userRequest = User.builder().id(1L).name("User1").email("user1@user.com").build();

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(userRequest);

        assertEquals(userService.create(userDtoRequest), expectedUserDto);

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void testUserCreateFailNoEmail() {
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}