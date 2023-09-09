package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exception.UserValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    private UserService userService;

    private UserController userController;

    private ObjectMapper mapper;

    private MockMvc mvc;

    private UserDto userDto;
    private UserDto expectedUserDto;

    private UserDto userWithoutEmail;

    @BeforeEach
    void init() {
        userService = Mockito.mock(UserService.class);
        userController = new UserController(userService);
        mapper = new ObjectMapper();
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
        userDto = UserDto.builder().name("User1").email("user1@user.com").build();
        expectedUserDto = UserDto.builder().id(1L).name("User1").email("user1@user.com").build();
        userWithoutEmail = UserDto.builder().name("User1").build();
    }


    @Test
    void create() throws Exception {
        Mockito.when(userService.create(Mockito.any(UserDto.class))).thenReturn(expectedUserDto);
        userService.create(userDto);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expectedUserDto.getName())))
                .andExpect(jsonPath("$.email", is(expectedUserDto.getEmail())));
    }

    /*
    @Test
    void createReturnUserValidateException() throws Exception {
        Mockito.when(userService.create(Mockito.any(UserDto.class))).thenThrow(UserValidationException.class);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userWithoutEmail))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

     */


    @Test
    void findById() throws Exception {
        Mockito.when(userService.findById(Mockito.anyLong())).thenReturn(expectedUserDto);

        userService.findById(1L);

        mvc.perform(get("/users/{id}", 1L))
                .andExpect(jsonPath("$.id", is(expectedUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expectedUserDto.getName())))
                .andExpect(jsonPath("$.email", is(expectedUserDto.getEmail())));
    }

    @Test
    void findAll() throws Exception {
        Mockito.when(userService.findAll()).thenReturn(List.of(expectedUserDto));

        userService.findAll();

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(expectedUserDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(expectedUserDto.getName())))
                .andExpect(jsonPath("$[0].email", is(expectedUserDto.getEmail())));
    }

    @Test
    void update() throws Exception {
        UserDto userUpdate = UserDto.builder().email("updated@user.com").build();
        expectedUserDto.setEmail("updated@user.com");
        Mockito.when(userService.update(Mockito.any(UserDto.class))).thenReturn(expectedUserDto);

        userService.update(userUpdate);

        String str = mvc.perform(patch("/users/{userId}", 1L)
                .content(mapper.writeValueAsString(userUpdate))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expectedUserDto.getName())))
                .andExpect(jsonPath("$.email", is(expectedUserDto.getEmail())))
                .andReturn().getResponse().getContentAsString();

        assertEquals(mapper.writeValueAsString(expectedUserDto), str);
    }

    @Test
    void deleteUserById() throws Exception {

        Mockito.doNothing().when(userService).delete(Mockito.anyLong());

        mvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).delete(Mockito.anyLong());
    }
}