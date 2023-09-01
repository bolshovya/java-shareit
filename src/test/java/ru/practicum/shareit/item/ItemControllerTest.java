package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ItemControllerTest {

    private ItemService itemService;

    private ItemController itemController;

    private ObjectMapper mapper;

    private MockMvc mvc;

    private UserDto userDto;
    private UserDto expectedUser;

    private ItemDto itemDto;
    private ItemDto expectedItem;

    private CommentDto commentDto;

    @BeforeEach
    void init() {
        itemService = Mockito.mock(ItemService.class);
        itemController = new ItemController(itemService);
        mapper = new ObjectMapper();
        mvc = MockMvcBuilders.standaloneSetup(itemController).build();
        userDto = UserDto.builder().name("User1").email("user1@user.com").build();
        expectedUser = UserDto.builder().id(1L).name("User1").email("user1@user.com").build();
        itemDto = ItemDto.builder().name("Дрель").description("Простая дрель").available(true).build();
        expectedItem = ItemDto.builder().id(1L).name("Дрель").description("Простая дрель").available(true).ownerId(1L).build();
        commentDto = CommentDto.builder().id(1L).text("text").authorName("User1").build();
    }

    @Test
    void create() throws Exception {
        Mockito.when(itemService.create(itemDto, 1L)).thenReturn(expectedItem);

        String str = mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedItem.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expectedItem.getName())))
                .andExpect(jsonPath("$.description", is(expectedItem.getDescription())))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void findById() throws Exception {
        Mockito.when(itemService.findById(Mockito.anyLong(), Mockito.anyLong())).thenReturn(expectedItem);

        mvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedItem.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expectedItem.getName())))
                .andExpect(jsonPath("$.description", is(expectedItem.getDescription())));

        Mockito.verify(itemService, Mockito.times(1)).findById(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    void findAll() throws Exception {
        Mockito.when(itemService.findAll(Mockito.anyLong())).thenReturn(List.of(expectedItem));

        mvc.perform(get("/items")
                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(expectedItem.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(expectedItem.getName())))
                .andExpect(jsonPath("$[0].description", is(expectedItem.getDescription())));

        Mockito.verify(itemService, Mockito.times(1)).findAll(Mockito.anyLong());
    }

    @Test
    void update() throws Exception {
        Mockito.when(itemService.update(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(ItemDto.class))).thenReturn(expectedItem);

        mvc.perform(patch("/items/{itemId}", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(itemService, Mockito.times(1)).update(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(ItemDto.class));
    }

    @Test
    void search() throws Exception {
        Mockito.when(itemService.search(Mockito.any(), Mockito.anyLong())).thenReturn(List.of(expectedItem));

        mvc.perform(get("/items/search")
                .param("text", "дрель")
                .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        Mockito.verify(itemService, Mockito.times(1)).search(Mockito.any(), Mockito.anyLong());
    }

    @Test
    void createComment() throws Exception {
        Mockito.when(itemService.createComment(Mockito.any(CommentDto.class), Mockito.anyLong(), Mockito.anyLong())).thenReturn(commentDto);

        mvc.perform(post("/items/{itemId}/comment", 1L)
                .content(mapper.writeValueAsString(commentDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .header("X-Sharer-User-Id", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(itemService, Mockito.times(1)).createComment(Mockito.any(CommentDto.class), Mockito.anyLong(), Mockito.anyLong());
    }
}