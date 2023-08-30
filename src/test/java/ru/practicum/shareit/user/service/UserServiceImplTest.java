package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

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
    void findById() {

        UserDto expectedUserDto = UserDto.builder().id(1L).name("User1").email("user1@user.com").build();

        User userRequest = User.builder().id(1L).name("User1").email("user1@user.com").build();

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userRequest));

        assertEquals(userService.findById(1L), expectedUserDto);

        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void findAll() {
        User user1 = User.builder().id(1L).name("User1").email("user1@user.com").build();
        User user2 = User.builder().id(2L).name("User2").email("user2@user.com").build();

        List<User> userList = List.of(user1, user2);

        Mockito.when(userRepository.findAll()).thenReturn(userList);

        assertEquals(userService.findAll().size(), 2);

        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    void update() {
        User userRequest = User.builder().id(1L).name("User1").email("user1@user.com").build();

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(userRequest);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userRequest));

        UserDto userDtoUpdate = UserDto.builder().id(1L).email("user1@user.com").build();

        assertEquals(userService.update(userDtoUpdate), UserMapper.getUserDto(userRequest));

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void delete() {
        User userRequest = User.builder().id(1L).name("User1").email("user1@user.com").build();

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userRequest));
        Mockito.doNothing().when(userRepository).deleteById(userRequest.getId());

        userService.delete(1L);

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
    }
}