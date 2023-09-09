package ru.practicum.shareit.booking.dto;

import lombok.*;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
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

    @AssertTrue(message = "")
    private boolean isEndAfterStart() {
        return start == null || end == null || end.isAfter(start);
    }

}