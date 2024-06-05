package ru.practicum.shareit.request.controller;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.comtroller.RequestController;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

@ContextConfiguration(classes = {RequestController.class})
@ExtendWith(SpringExtension.class)
class RequestControllerTest {
    @Autowired
    private RequestController requestController;

    @MockBean
    private RequestService requestService;

    @Test
    void testCreate() throws Exception {
        RequestDto.RequestDtoBuilder builderResult = RequestDto.builder();
        RequestDto.RequestDtoBuilder idResult = builderResult.created(LocalDate.of(1970, 1, 1).atStartOfDay())
                .description("The characteristics of someone or something")
                .id(1L);
        RequestDto buildResult = idResult.items(new ArrayList<>()).build();
        when(requestService.create(Mockito.<Long>any(), Mockito.<RequestCreateDto>any())).thenReturn(buildResult);

        RequestCreateDto requestCreateDto = new RequestCreateDto();
        requestCreateDto.setDescription("The characteristics of someone or something");
        String content = (new ObjectMapper()).writeValueAsString(requestCreateDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/requests")
                .header("X-Sharer-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(requestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"description\":\"The characteristics of someone or something\",\"created\":[1970,1,1,0,0],"
                                        + "\"items\":[]}"));
    }

    @Test
    void testCreate2() throws Exception {
        RequestDto.RequestDtoBuilder builderResult = RequestDto.builder();
        RequestDto.RequestDtoBuilder idResult = builderResult.created(LocalDate.of(1970, 1, 1).atStartOfDay())
                .description("The characteristics of someone or something")
                .id(1L);
        RequestDto buildResult = idResult.items(new ArrayList<>()).build();
        when(requestService.create(Mockito.<Long>any(), Mockito.<RequestCreateDto>any())).thenReturn(buildResult);

        RequestCreateDto requestCreateDto = new RequestCreateDto();
        requestCreateDto.setDescription("");
        String content = (new ObjectMapper()).writeValueAsString(requestCreateDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/requests")
                .header("X-Sharer-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(requestController)
                .build()
                .perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void testGetAll() throws Exception {
        when(requestService.getAll(Mockito.<Long>any(), Mockito.<Pageable>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/requests/all");
        MockHttpServletRequestBuilder paramResult = getResult.param("from", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1))
                .header("X-Sharer-User-Id", "42");

        MockMvcBuilders.standaloneSetup(requestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGetById() throws Exception {
        RequestDto.RequestDtoBuilder builderResult = RequestDto.builder();
        RequestDto.RequestDtoBuilder idResult = builderResult.created(LocalDate.of(1970, 1, 1).atStartOfDay())
                .description("The characteristics of someone or something")
                .id(1L);
        RequestDto buildResult = idResult.items(new ArrayList<>()).build();
        when(requestService.getById(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(buildResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/requests/{requestId}", 1L)
                .header("X-Sharer-User-Id", "42");

        MockMvcBuilders.standaloneSetup(requestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"description\":\"The characteristics of someone or something\",\"created\":[1970,1,1,0,0],"
                                        + "\"items\":[]}"));
    }

    @Test
    void testGetAllUser() throws Exception {
        when(requestService.getAllUserById(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/requests")
                .header("X-Sharer-User-Id", "42");

        MockMvcBuilders.standaloneSetup(requestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}
