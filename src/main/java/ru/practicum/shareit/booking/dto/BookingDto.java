package ru.practicum.shareit.booking.dto;


import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.Transient;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {

    private long id;

    @NotNull
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull
    @Future
    private LocalDateTime end;

    private Long itemId;

    private Item item;

    private Long bookerId;

    private User booker;

    private BookingStatus status;
}
