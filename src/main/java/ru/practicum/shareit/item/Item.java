package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class Item {
    private long id;
    private long userId; // владелец
    private String name;
    private String description;
    private Boolean available; // Статус должен проставлять владелец
}
