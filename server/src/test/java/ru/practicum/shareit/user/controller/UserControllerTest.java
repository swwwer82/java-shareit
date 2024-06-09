package ru.practicum.shareit.user.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.exeption.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTest {

    private static final String URL_BASE = "/users";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserController userController;

    private UserDto createDefaultUser() {
        return userController.userService.create(UserCreateDto.builder()
                .email(generateEmail())
                .name(UUID.randomUUID().toString())
                .build());
    }

    private String generateEmail() {
        return String.format("%s@%s", UUID.randomUUID(), UUID.randomUUID());
    }

    private String prepareBody(String name, String email) {
        return "{\"name\": \"" + name + "\", \"email\": \"" + email + "\"}";
    }

    @Test
    void getAllSuccess() throws Exception {
        createDefaultUser();
        createDefaultUser();

        assertNotEquals("[]", mockMvc.perform(get(URL_BASE).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());
    }

    @Test
    void getSuccess() throws Exception {
        UserDto user = createDefaultUser();

        mockMvc.perform(get(URL_BASE + "/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void createSuccess() throws Exception {
        String name = UUID.randomUUID().toString();
        String email = generateEmail();

        mockMvc.perform(post(URL_BASE)
                        .content(prepareBody(name, email))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void update() throws Exception {
        UserDto user = createDefaultUser();

        String name = UUID.randomUUID().toString();
        String email = generateEmail();

        mockMvc.perform(patch(URL_BASE + "/" + user.getId())
                        .content(prepareBody(name, email))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        UserDto userUpdated = userController.userService.get(user.getId());

        assertEquals(user.getId(), userUpdated.getId());
        assertEquals(name, userUpdated.getName());
        assertEquals(email, userUpdated.getEmail());
    }

    @Test
    void deleteSuccess() throws Exception {
        UserDto user = createDefaultUser();

        mockMvc.perform(delete(URL_BASE + "/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThrows(NotFoundException.class, () -> userController.userService.get(user.getId()));
    }
}