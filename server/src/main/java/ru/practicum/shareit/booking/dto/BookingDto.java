package ru.practicum.shareit.booking.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;


@Builder
@Getter
@Setter
@ToString
public class BookingDto {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long itemId;

    private ItemDto item;

    private Long bookerId;

    private UserDto booker;

    private BookingState status;
}
