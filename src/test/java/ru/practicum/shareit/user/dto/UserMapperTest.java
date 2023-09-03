package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void getUserDto() {
        Item item1 = Item.builder().id(1L).name("Дрель").description("Простая дрель").available(true).build();
        User user = User.builder().id(1L).name("User").email("Email@email.ru").items(List.of(item1)).build();

        UserDto actual = UserMapper.getUserDto(user);
    }

    @Test
    void getUser() {
        UserDto userDto = UserDto.builder().id(1L).name("name").email("email@email.ru").build();

        User actual = UserMapper.getUser(userDto);
    }
}