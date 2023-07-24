package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.User;

public class UserMapper {

    public static UserDto getUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User getUser(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

}
