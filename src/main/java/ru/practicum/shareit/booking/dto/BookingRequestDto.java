package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class BookingRequestDto {

    private Long itemId;

    @NotNull
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull
    @Future
    private LocalDateTime end;
}
