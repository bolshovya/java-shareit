package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private Long ownerId;

    @NotNull
    private Boolean available;

    private ItemBookingDto lastBooking;

    private ItemBookingDto nextBooking;

    private List<CommentDto> comments;

    private Long requestId;
}
