package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookingControllerTest {

    private BookingController bookingController;

    private BookingService bookingService;

    private ObjectMapper mapper;

    private MockMvc mvc;

    private BookingRequestDto bookingRequestDto;

    private UserDto userDto;
    private ItemDto itemDto;

    private BookingDto bookingDto;

    @BeforeEach
    void init() {
        bookingService = Mockito.mock(BookingServiceImpl.class);
        bookingController = new BookingController(bookingService);
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        bookingRequestDto = BookingRequestDto.builder().itemId(1L).start(start).end(end).build();
        userDto = UserDto.builder().id(1L).name("User1").email("user1@user.com").build();
        itemDto = ItemDto.builder().id(1L).name("Дрель").description("Простая дрель").available(true).build();
        bookingDto = BookingDto.builder().id(1L).item(itemDto).booker(userDto).status(BookingStatus.WAITING).start(start).end(end).build();
    }

    @Test
    void create() throws Exception {
        Mockito.when(bookingService.create(Mockito.any(BookingRequestDto.class), Mockito.anyLong())).thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        Mockito.verify(bookingService, Mockito.times(1)).create(Mockito.any(BookingRequestDto.class), Mockito.anyLong());
    }

    @Test
    void updateBookingStatus() throws Exception {

        Mockito.when(bookingService.updateBookingStatus(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong())).thenReturn(bookingDto);

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                .header("X-Sharer-User-Id", 1L)
                .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class));

        Mockito.verify(bookingService, Mockito.times(1)).updateBookingStatus(1L, "true", 1L);

    }

    @Test
    void getBooking() throws Exception {

        Mockito.when(bookingService.getBooking(Mockito.anyLong(), Mockito.anyLong())).thenReturn(bookingDto);

        mvc.perform(get("/bookings/{bookingId}", 1L)
                .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class));

        Mockito.verify(bookingService, Mockito.times(1)).getBooking(1L, 1L);
    }

    @Test
    void getAllByBooker() throws Exception {

        Mockito.when(bookingService.getAllByBooker(Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings")
                .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", String.valueOf(1))
                        .param("size", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class));

        Mockito.verify(bookingService, Mockito.times(1)).getAllByBooker("ALL", 1L, 1, 1);
    }

    @Test
    void getAllBookingsForAllItemsOfOwner() throws Exception {

        Mockito.when(bookingService.getAllBookingsForAllItemsOfOwner(Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner")
                .header("X-Sharer-User-Id", 1L)
                .param("state", "ALL")
                .param("from", String.valueOf(1))
                .param("size", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class));

        Mockito.verify(bookingService, Mockito.times(1)).getAllBookingsForAllItemsOfOwner("ALL", 1L, 1, 1);
    }
}