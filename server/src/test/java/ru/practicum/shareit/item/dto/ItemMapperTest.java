package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

class ItemMapperTest {

    @Test
    void getItem() {
        ItemDto itemDto = ItemDto.builder()
                .name("Drel")
                .description("simple drel")
                .available(true)
                .build();

        Item item = ItemMapper.getItem(itemDto);
    }

    @Test
    void getItemDto() {

        Item item = Item.builder()
                .id(1L)
                .name("Drel")
                .description("simple drel")
                .owner(User.builder().id(1L).build())
                .available(true)
                .request(ItemRequest.builder().id(1L).build())
                .build();

        ItemDto actual = ItemMapper.getItemDto(item);
    }

    @Test
    void getItemBookingDto() {

        Booking booking = Booking.builder()
                .id(1L)
                .booker(User.builder().id(1L).build())
                .build();

        ItemBookingDto actual = ItemMapper.getItemBookingDto(booking);
    }

    @Test
    void getItemRequestResponseDto() {

        Item item = Item.builder()
                .id(1L)
                .name("Drel")
                .description("simple drel")
                .owner(User.builder().id(1L).build())
                .available(true)
                .request(ItemRequest.builder().id(1L).build())
                .build();

        ItemRequestResponseDto itemRequestResponseDto = ItemMapper.getItemRequestResponseDto(item);

    }
}