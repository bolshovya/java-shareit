package ru.practicum.shareit.user.service;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.exception.UserValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserServiceTest {

    private UserService userService;

    private UserRepository userRepository;

    private UserDto expectedUserDto;

    private UserDto userDtoRequest;
    private User userRequest;

    @BeforeEach
    void init() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
        expectedUserDto = UserDto.builder().id(1L).name("User1").email("user1@user.com").build();
        userDtoRequest = UserDto.builder().name("User1").email("user1@user.com").build();
        userRequest = User.builder().id(1L).name("User1").email("user1@user.com").build();
    }

    @Test
    void testUserCreate() {
        Mockito.when(
                userRepository
                        .save(Mockito.any(User.class)))
                        .thenReturn(userRequest);

        UserDto userDtoFromDb = userService.create(userDtoRequest);

        assertEquals(userDtoFromDb, expectedUserDto);

        Mockito.verify(
                userRepository,
                Mockito.times(1))
                .save(Mockito.any());
    }

    @Test
    void testUserCreateFailWithoutEmail() {
        Mockito.when(
                userRepository
                        .save(Mockito.any(User.class)))
                .thenThrow(ConstraintViolationException.class);

        UserDto userWithOutEmail = UserDto.builder().name("User123").build();

        Assertions.assertThrows(UserValidationException.class, () -> userService.create(userWithOutEmail));
    }



    @Test
    void findById() {
        Mockito.when(
                userRepository
                        .findById(Mockito.anyLong()))
                        .thenReturn(Optional.of(userRequest));

        assertEquals(userService.findById(1L), expectedUserDto);

        Mockito.verify(
                userRepository,
                Mockito.times(1))
                .findById(1L);
    }

    @Test
    void findAll() {
        User user1 = User.builder().id(1L).name("User1").email("user1@user.com").build();
        User user2 = User.builder().id(2L).name("User2").email("user2@user.com").build();

        List<User> userList = List.of(user1, user2);

        Mockito.when(
                userRepository
                        .findAll())
                        .thenReturn(userList);

        assertEquals(userService.findAll().size(), 2);

        Mockito.verify(
                userRepository,
                Mockito.times(1))
                .findAll();
    }

    @Test
    void updateEmail() {
        Mockito.when(
                userRepository
                        .save(Mockito.any(User.class)))
                        .thenReturn(userRequest);

        Mockito.when(
                userRepository
                        .findById(Mockito.anyLong()))
                        .thenReturn(Optional.of(userRequest));

        UserDto userDtoUpdate = UserDto.builder().id(1L).email("user1@user.com").build();

        assertEquals(userService.update(userDtoUpdate), UserMapper.getUserDto(userRequest));

        Mockito.verify(
                userRepository,
                Mockito.times(1))
                .save(Mockito.any(User.class));

        Mockito.verify(
                userRepository,
                Mockito.times(1))
                .findById(Mockito.anyLong());
    }

    @Test
    void updateName() {
        Mockito.when(
                        userRepository
                                .save(Mockito.any(User.class)))
                .thenReturn(userRequest);

        Mockito.when(
                        userRepository
                                .findById(Mockito.anyLong()))
                .thenReturn(Optional.of(userRequest));

        UserDto userDtoUpdate = UserDto.builder().id(1L).name("UserUpdate").build();

        assertEquals(userService.update(userDtoUpdate), UserMapper.getUserDto(userRequest));

        Mockito.verify(
                        userRepository,
                        Mockito.times(1))
                .save(Mockito.any(User.class));

        Mockito.verify(
                        userRepository,
                        Mockito.times(1))
                .findById(Mockito.anyLong());
    }

    @Test
    void delete() {
        Mockito.when(
                userRepository
                        .findById(Mockito.anyLong()))
                .thenReturn(Optional.of(userRequest));

        Mockito.doNothing().when(userRepository).deleteById(userRequest.getId());

        userService.delete(1L);

        Mockito.verify(
                userRepository,
                Mockito.times(1))
                .findById(Mockito.anyLong());

        Mockito.verify(
                userRepository,
                Mockito.times(1))
                .deleteById(1L);
    }
}