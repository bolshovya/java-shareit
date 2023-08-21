package ru.practicum.shareit.booking.dto;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.Transient;
import javax.validation.constraints.*;
import java.time.LocalDateTime;


@Builder
@Getter
@Setter
public class BookingDto {

    private Long id;

    @NotNull
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull
    @Future
    private LocalDateTime end;

    private Long itemId;

    private ItemDto item;

    private Long bookerId;

    private UserDto booker;

    private BookingStatus status;
}
