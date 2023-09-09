package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ItemRequestResponseDto {

    private Long id;

    private String name;

    private String description;

    private Long requestId;

    private Boolean available;
}