package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ItemRequestControllerTest {

    private ItemRequestController itemRequestController;

    private ItemRequestService itemRequestService;

    private ObjectMapper mapper;

    private MockMvc mvc;

    private ItemRequestDto itemRequestDto;

    private ItemRequestDto expectedItemRequestDto;



    @BeforeEach
    void init() {
        itemRequestService = Mockito.mock(ItemRequestServiceImpl.class);
        itemRequestController = new ItemRequestController(itemRequestService);
        mapper = new ObjectMapper();
        mvc = MockMvcBuilders.standaloneSetup(itemRequestController).build();
        itemRequestDto = ItemRequestDto.builder().id(1L).description("description").build();
        expectedItemRequestDto = ItemRequestDto.builder().id(1L).description("description").build();
    }


    @Test
    void create() throws Exception {

        Mockito.when(
                itemRequestService
                        .create(Mockito.any(ItemRequestDto.class), Mockito.anyLong()))
                        .thenReturn(expectedItemRequestDto);

        mvc.perform(post("/requests")
                .content(mapper.writeValueAsString(itemRequestDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedItemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(expectedItemRequestDto.getDescription())));

        Mockito.verify(
                itemRequestService,
                Mockito.times(1))
                .create(Mockito.any(ItemRequestDto.class), Mockito.anyLong());
    }

    @Test
    void findAllOfYour() throws Exception {

        Mockito.when(
                itemRequestService
                        .findAllOfYour(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                        .thenReturn(List.of(expectedItemRequestDto));

        mvc.perform(get("/requests")
                .header("X-Sharer-User-Id", 1L)
                .param("from", String.valueOf(1))
                .param("size", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(expectedItemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(expectedItemRequestDto.getDescription())));

        Mockito.verify(
                itemRequestService,
                Mockito.times(1))
                .findAllOfYour(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    void findAllOfOther() throws Exception {

        Mockito.when(
                itemRequestService
                        .findAllOfOther(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(expectedItemRequestDto));

        mvc.perform(get("/requests/all")
                .header("X-Sharer-User-Id", 1L)
                .param("from", String.valueOf(1))
                .param("size", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(expectedItemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(expectedItemRequestDto.getDescription())));

        Mockito.verify(
                itemRequestService,
                Mockito.times(1))
                .findAllOfOther(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    void findById() throws Exception {

        Mockito.when(
                itemRequestService
                        .findById(Mockito.anyLong(), Mockito.anyLong()))
                        .thenReturn(expectedItemRequestDto);

        mvc.perform(get("/requests/{requestId}", 1L)
                .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedItemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(expectedItemRequestDto.getDescription())));

        Mockito.verify(
                itemRequestService,
                Mockito.times(1))
                .findById(Mockito.anyLong(), Mockito.anyLong());
    }
}