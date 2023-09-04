package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Item;

@UtilityClass
public class ItemMapper {

    public static Item getItem(ItemDto itemDto) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    public static ItemDto getItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(item.getOwner().getId())
                .available(item.getAvailable())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

    public static ItemBookingDto getItemBookingDto(Booking booking) {
        return ItemBookingDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }

    public static ItemRequestResponseDto getItemRequestResponseDto(Item item) {
        return ItemRequestResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .requestId(item.getRequest().getId())
                .available(item.getAvailable())
                .build();
    }
}
