package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {ItemController.class})
@ExtendWith(SpringExtension.class)
class ItemControllerTest {
    @Autowired
    private ItemController itemController;

    @MockBean
    private ItemService itemService;

    @Test
    void testGetAll() throws Exception {
        when(itemService.getAll(Mockito.<Long>any(), Mockito.any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/items");
        MockHttpServletRequestBuilder paramResult = getResult.param("from", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1))
                .header("X-Sharer-User-Id", "42");

        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGet() throws Exception {
        ItemDto.ItemDtoBuilder availableResult = ItemDto.builder().available(true);
        ItemDto.ItemDtoBuilder idResult = availableResult.comments(new ArrayList<>())
                .description("The characteristics of someone or something")
                .id(1L);
        BookingItemDto.BookingItemDtoBuilder bookerIdResult = BookingItemDto.builder().bookerId(1L);
        BookingItemDto.BookingItemDtoBuilder idResult2 = bookerIdResult.endDate(LocalDate.of(1970, 1, 1).atStartOfDay())
                .id(1L);
        BookingItemDto lastBooking = idResult2.startDate(LocalDate.of(1970, 1, 1).atStartOfDay()).build();
        ItemDto.ItemDtoBuilder nameResult = idResult.lastBooking(lastBooking).name("Name");
        BookingItemDto.BookingItemDtoBuilder bookerIdResult2 = BookingItemDto.builder().bookerId(1L);
        BookingItemDto.BookingItemDtoBuilder idResult3 = bookerIdResult2.endDate(LocalDate.of(1970, 1, 1).atStartOfDay())
                .id(1L);
        BookingItemDto nextBooking = idResult3.startDate(LocalDate.of(1970, 1, 1).atStartOfDay()).build();
        ItemDto buildResult = nameResult.nextBooking(nextBooking).requestId(1L).build();
        when(itemService.get(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(buildResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/items/{id}", 1L)
                .header("X-Sharer-User-Id", "42");

        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"name\":\"Name\",\"description\":\"The characteristics of someone or something\",\"available\":true"
                                        + ",\"lastBooking\":{\"id\":1,\"bookerId\":1,\"startDate\":[1970,1,1,0,0],\"endDate\":[1970,1,1,0,0]},\"nextBooking"
                                        + "\":{\"id\":1,\"bookerId\":1,\"startDate\":[1970,1,1,0,0],\"endDate\":[1970,1,1,0,0]},\"comments\":[],\"requestId"
                                        + "\":1}"));
    }

    @Test
    void testCreate() throws Exception {
        ItemDto.ItemDtoBuilder availableResult = ItemDto.builder().available(true);
        ItemDto.ItemDtoBuilder idResult = availableResult.comments(new ArrayList<>())
                .description("The characteristics of someone or something")
                .id(1L);
        BookingItemDto.BookingItemDtoBuilder bookerIdResult = BookingItemDto.builder().bookerId(1L);
        BookingItemDto.BookingItemDtoBuilder idResult2 = bookerIdResult.endDate(LocalDate.of(1970, 1, 1).atStartOfDay())
                .id(1L);
        BookingItemDto lastBooking = idResult2.startDate(LocalDate.of(1970, 1, 1).atStartOfDay()).build();
        ItemDto.ItemDtoBuilder nameResult = idResult.lastBooking(lastBooking).name("Name");
        BookingItemDto.BookingItemDtoBuilder bookerIdResult2 = BookingItemDto.builder().bookerId(1L);
        BookingItemDto.BookingItemDtoBuilder idResult3 = bookerIdResult2.endDate(LocalDate.of(1970, 1, 1).atStartOfDay())
                .id(1L);
        BookingItemDto nextBooking = idResult3.startDate(LocalDate.of(1970, 1, 1).atStartOfDay()).build();
        ItemDto buildResult = nameResult.nextBooking(nextBooking).requestId(1L).build();
        when(itemService.create(Mockito.<Long>any(), Mockito.any())).thenReturn(buildResult);

        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setAvailable(true);
        itemCreateDto.setDescription("The characteristics of someone or something");
        itemCreateDto.setName("Name");
        itemCreateDto.setRequestId(1L);
        String content = (new ObjectMapper()).writeValueAsString(itemCreateDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/items")
                .header("X-Sharer-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"name\":\"Name\",\"description\":\"The characteristics of someone or something\",\"available\":true"
                                        + ",\"lastBooking\":{\"id\":1,\"bookerId\":1,\"startDate\":[1970,1,1,0,0],\"endDate\":[1970,1,1,0,0]},\"nextBooking"
                                        + "\":{\"id\":1,\"bookerId\":1,\"startDate\":[1970,1,1,0,0],\"endDate\":[1970,1,1,0,0]},\"comments\":[],\"requestId"
                                        + "\":1}"));
    }

    @Test
    void testCreate2() throws Exception {
        ItemDto.ItemDtoBuilder availableResult = ItemDto.builder().available(true);
        ItemDto.ItemDtoBuilder idResult = availableResult.comments(new ArrayList<>())
                .description("The characteristics of someone or something")
                .id(1L);
        BookingItemDto.BookingItemDtoBuilder bookerIdResult = BookingItemDto.builder().bookerId(1L);
        BookingItemDto.BookingItemDtoBuilder idResult2 = bookerIdResult.endDate(LocalDate.of(1970, 1, 1).atStartOfDay())
                .id(1L);
        BookingItemDto lastBooking = idResult2.startDate(LocalDate.of(1970, 1, 1).atStartOfDay()).build();
        ItemDto.ItemDtoBuilder nameResult = idResult.lastBooking(lastBooking).name("Name");
        BookingItemDto.BookingItemDtoBuilder bookerIdResult2 = BookingItemDto.builder().bookerId(1L);
        BookingItemDto.BookingItemDtoBuilder idResult3 = bookerIdResult2.endDate(LocalDate.of(1970, 1, 1).atStartOfDay())
                .id(1L);
        BookingItemDto nextBooking = idResult3.startDate(LocalDate.of(1970, 1, 1).atStartOfDay()).build();
        ItemDto buildResult = nameResult.nextBooking(nextBooking).requestId(1L).build();
        when(itemService.create(Mockito.<Long>any(), Mockito.any())).thenReturn(buildResult);

        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setAvailable(true);
        itemCreateDto.setDescription("");
        itemCreateDto.setName("Name");
        itemCreateDto.setRequestId(1L);
        String content = (new ObjectMapper()).writeValueAsString(itemCreateDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/items")
                .header("X-Sharer-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(itemController).build().perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void testUpdate() throws Exception {
        ItemDto.ItemDtoBuilder availableResult = ItemDto.builder().available(true);
        ItemDto.ItemDtoBuilder idResult = availableResult.comments(new ArrayList<>())
                .description("The characteristics of someone or something")
                .id(1L);
        BookingItemDto.BookingItemDtoBuilder bookerIdResult = BookingItemDto.builder().bookerId(1L);
        BookingItemDto.BookingItemDtoBuilder idResult2 = bookerIdResult.endDate(LocalDate.of(1970, 1, 1).atStartOfDay())
                .id(1L);
        BookingItemDto lastBooking = idResult2.startDate(LocalDate.of(1970, 1, 1).atStartOfDay()).build();
        ItemDto.ItemDtoBuilder nameResult = idResult.lastBooking(lastBooking).name("Name");
        BookingItemDto.BookingItemDtoBuilder bookerIdResult2 = BookingItemDto.builder().bookerId(1L);
        BookingItemDto.BookingItemDtoBuilder idResult3 = bookerIdResult2.endDate(LocalDate.of(1970, 1, 1).atStartOfDay())
                .id(1L);
        BookingItemDto nextBooking = idResult3.startDate(LocalDate.of(1970, 1, 1).atStartOfDay()).build();
        ItemDto buildResult = nameResult.nextBooking(nextBooking).requestId(1L).build();
        when(itemService.update(Mockito.<Long>any(), Mockito.<Long>any(), Mockito.any()))
                .thenReturn(buildResult);

        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setAvailable(true);
        itemUpdateDto.setDescription("The characteristics of someone or something");
        itemUpdateDto.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(itemUpdateDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/items/{id}", 1L)
                .header("X-Sharer-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"name\":\"Name\",\"description\":\"The characteristics of someone or something\",\"available\":true"
                                        + ",\"lastBooking\":{\"id\":1,\"bookerId\":1,\"startDate\":[1970,1,1,0,0],\"endDate\":[1970,1,1,0,0]},\"nextBooking"
                                        + "\":{\"id\":1,\"bookerId\":1,\"startDate\":[1970,1,1,0,0],\"endDate\":[1970,1,1,0,0]},\"comments\":[],\"requestId"
                                        + "\":1}"));
    }

    @Test
    void testSearch() throws Exception {
        when(itemService.search(Mockito.any(), Mockito.any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/items/search");
        MockHttpServletRequestBuilder paramResult = getResult.param("from", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1)).param("text", "foo");

        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testCreateComment() throws Exception {
        CommentDto.CommentDtoBuilder authorNameResult = CommentDto.builder().authorName("JaneDoe");
        CommentDto buildResult = authorNameResult.created(LocalDate.of(1970, 1, 1).atStartOfDay())
                .id(1L)
                .text("Text")
                .build();
        when(itemService.createComment(Mockito.<Long>any(), Mockito.<Long>any(), Mockito.any()))
                .thenReturn(buildResult);

        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText("Text");
        String content = (new ObjectMapper()).writeValueAsString(commentCreateDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/items/{itemId}/comment", 1L)
                .header("X-Sharer-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":1,\"text\":\"Text\",\"authorName\":\"JaneDoe\",\"created\":[1970,1,1,0,0]}"));
    }
}
