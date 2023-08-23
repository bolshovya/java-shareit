package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
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

    ItemBookingDto lastBooking;

    ItemBookingDto nextBooking;

    List<CommentDto> comments;
}
