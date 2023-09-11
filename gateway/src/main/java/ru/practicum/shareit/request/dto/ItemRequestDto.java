package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

    private Long id;

    @NotBlank
    private String description;

    private UserDto requestor;

    private LocalDateTime created;

    private List<ItemRequestResponseDto> items;
}