package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {

    private Long id;

    private String name;

    private String description;

    private Long ownerId;

    private Boolean available;

    private ItemBookingDto lastBooking;

    private ItemBookingDto nextBooking;

    private List<CommentDto> comments;

    private Long requestId;
}