package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testItemDto() throws Exception {

        ItemBookingDto itemBookingDto = ItemBookingDto.builder()
                .id(1L)
                .bookerId(1L)
                .build();

        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("text")
                .authorName("User")
                .created(LocalDateTime.now())
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Простая дрель")
                .ownerId(1L)
                .available(true)
                .lastBooking(itemBookingDto)
                .nextBooking(itemBookingDto)
                .comments(List.of(commentDto))
                .requestId(1L)
                .build();

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo(itemDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemDto.getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.ownerId")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(itemDto.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.bookerId")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.bookerId")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.comments.[0]id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.comments.[0]text")
                .isEqualTo(commentDto.getText());
        assertThat(result).extractingJsonPathStringValue("$.comments.[0]authorName")
                .isEqualTo(commentDto.getAuthorName());
    }


}
