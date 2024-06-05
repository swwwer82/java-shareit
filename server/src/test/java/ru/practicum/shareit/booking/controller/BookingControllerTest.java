package ru.practicum.shareit.booking.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.util.BookingState;
import ru.practicum.shareit.booking.util.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

@ContextConfiguration(classes = {BookingController.class})
@ExtendWith(SpringExtension.class)
class BookingControllerTest {
    @Autowired
    private BookingController bookingController;

    @MockBean
    private BookingService bookingService;

    @Test
    void testCreate() {
        BookingService bookingService = mock(BookingService.class);
        BookingDto.BookingDtoBuilder builderResult = BookingDto.builder();
        UserDto booker = UserDto.builder().email("jane.doe@example.org").id(1L).name("Name").build();
        BookingDto.BookingDtoBuilder bookerResult = builderResult.booker(booker);
        BookingDto.BookingDtoBuilder idResult = bookerResult.end(LocalDate.of(1970, 1, 1).atStartOfDay()).id(1L);
        ItemDto.ItemDtoBuilder availableResult = ItemDto.builder().available(true);
        ItemDto.ItemDtoBuilder idResult2 = availableResult.comments(new ArrayList<>())
                .description("The characteristics of someone or something")
                .id(1L);
        BookingItemDto.BookingItemDtoBuilder bookerIdResult = BookingItemDto.builder().bookerId(1L);
        BookingItemDto.BookingItemDtoBuilder idResult3 = bookerIdResult.endDate(LocalDate.of(1970, 1, 1).atStartOfDay())
                .id(1L);
        BookingItemDto lastBooking = idResult3.startDate(LocalDate.of(1970, 1, 1).atStartOfDay()).build();
        ItemDto.ItemDtoBuilder nameResult = idResult2.lastBooking(lastBooking).name("Name");
        BookingItemDto.BookingItemDtoBuilder bookerIdResult2 = BookingItemDto.builder().bookerId(1L);
        BookingItemDto.BookingItemDtoBuilder idResult4 = bookerIdResult2.endDate(LocalDate.of(1970, 1, 1).atStartOfDay())
                .id(1L);
        BookingItemDto nextBooking = idResult4.startDate(LocalDate.of(1970, 1, 1).atStartOfDay()).build();
        ItemDto item = nameResult.nextBooking(nextBooking).requestId(1L).build();
        BookingDto.BookingDtoBuilder itemResult = idResult.item(item);
        BookingDto buildResult = itemResult.start(LocalDate.of(1970, 1, 1).atStartOfDay())
                .status(BookingStatus.WAITING)
                .build();
        when(bookingService.create(Mockito.<Long>any(), Mockito.<BookingCreateDto>any())).thenReturn(buildResult);
        BookingController bookingController = new BookingController(bookingService);

        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setEnd(LocalDate.of(1970, 1, 1).atStartOfDay());
        bookingCreateDto.setItemId(1L);
        bookingCreateDto.setStart(LocalDate.of(1970, 1, 1).atStartOfDay());

        ResponseEntity<BookingDto> actualCreateResult = bookingController.create(1L, bookingCreateDto);

        verify(bookingService).create(Mockito.<Long>any(), Mockito.<BookingCreateDto>any());
        assertEquals(HttpStatus.OK, actualCreateResult.getStatusCode());
        assertTrue(actualCreateResult.hasBody());
        assertTrue(actualCreateResult.getHeaders().isEmpty());
    }

    @Test
    void testApproveBooking() throws Exception {
        BookingDto.BookingDtoBuilder builderResult = BookingDto.builder();
        UserDto booker = UserDto.builder().email("jane.doe@example.org").id(1L).name("Name").build();
        BookingDto.BookingDtoBuilder bookerResult = builderResult.booker(booker);
        BookingDto.BookingDtoBuilder idResult = bookerResult.end(LocalDate.of(1970, 1, 1).atStartOfDay()).id(1L);
        ItemDto.ItemDtoBuilder availableResult = ItemDto.builder().available(true);
        ItemDto.ItemDtoBuilder idResult2 = availableResult.comments(new ArrayList<>())
                .description("The characteristics of someone or something")
                .id(1L);
        BookingItemDto.BookingItemDtoBuilder bookerIdResult = BookingItemDto.builder().bookerId(1L);
        BookingItemDto.BookingItemDtoBuilder idResult3 = bookerIdResult.endDate(LocalDate.of(1970, 1, 1).atStartOfDay())
                .id(1L);
        BookingItemDto lastBooking = idResult3.startDate(LocalDate.of(1970, 1, 1).atStartOfDay()).build();
        ItemDto.ItemDtoBuilder nameResult = idResult2.lastBooking(lastBooking).name("Name");
        BookingItemDto.BookingItemDtoBuilder bookerIdResult2 = BookingItemDto.builder().bookerId(1L);
        BookingItemDto.BookingItemDtoBuilder idResult4 = bookerIdResult2.endDate(LocalDate.of(1970, 1, 1).atStartOfDay())
                .id(1L);
        BookingItemDto nextBooking = idResult4.startDate(LocalDate.of(1970, 1, 1).atStartOfDay()).build();
        ItemDto item = nameResult.nextBooking(nextBooking).requestId(1L).build();
        BookingDto.BookingDtoBuilder itemResult = idResult.item(item);
        BookingDto buildResult = itemResult.start(LocalDate.of(1970, 1, 1).atStartOfDay())
                .status(BookingStatus.WAITING)
                .build();
        when(bookingService.approveBooking(Mockito.<Long>any(), Mockito.<Long>any(), anyBoolean())).thenReturn(buildResult);
        MockHttpServletRequestBuilder patchResult = MockMvcRequestBuilders.patch("/bookings/{bookingId}", 1L);
        MockHttpServletRequestBuilder requestBuilder = patchResult.param("approved", String.valueOf(true))
                .header("X-Sharer-User-Id", "42");

        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"start\":[1970,1,1,0,0],\"end\":[1970,1,1,0,0],\"status\":\"WAITING\",\"booker\":{\"id\":1,\"email\":\"jane"
                                        + ".doe@example.org\",\"name\":\"Name\"},\"item\":{\"id\":1,\"name\":\"Name\",\"description\":\"The characteristics of"
                                        + " someone or something\",\"available\":true,\"lastBooking\":{\"id\":1,\"bookerId\":1,\"startDate\":[1970,1,1,0,0"
                                        + "],\"endDate\":[1970,1,1,0,0]},\"nextBooking\":{\"id\":1,\"bookerId\":1,\"startDate\":[1970,1,1,0,0],\"endDate\":"
                                        + "[1970,1,1,0,0]},\"comments\":[],\"requestId\":1}}"));
    }

    @Test
    void testGet() throws Exception {
        BookingDto.BookingDtoBuilder builderResult = BookingDto.builder();
        UserDto booker = UserDto.builder().email("jane.doe@example.org").id(1L).name("Name").build();
        BookingDto.BookingDtoBuilder bookerResult = builderResult.booker(booker);
        BookingDto.BookingDtoBuilder idResult = bookerResult.end(LocalDate.of(1970, 1, 1).atStartOfDay()).id(1L);
        ItemDto.ItemDtoBuilder availableResult = ItemDto.builder().available(true);
        ItemDto.ItemDtoBuilder idResult2 = availableResult.comments(new ArrayList<>())
                .description("The characteristics of someone or something")
                .id(1L);
        BookingItemDto.BookingItemDtoBuilder bookerIdResult = BookingItemDto.builder().bookerId(1L);
        BookingItemDto.BookingItemDtoBuilder idResult3 = bookerIdResult.endDate(LocalDate.of(1970, 1, 1).atStartOfDay())
                .id(1L);
        BookingItemDto lastBooking = idResult3.startDate(LocalDate.of(1970, 1, 1).atStartOfDay()).build();
        ItemDto.ItemDtoBuilder nameResult = idResult2.lastBooking(lastBooking).name("Name");
        BookingItemDto.BookingItemDtoBuilder bookerIdResult2 = BookingItemDto.builder().bookerId(1L);
        BookingItemDto.BookingItemDtoBuilder idResult4 = bookerIdResult2.endDate(LocalDate.of(1970, 1, 1).atStartOfDay())
                .id(1L);
        BookingItemDto nextBooking = idResult4.startDate(LocalDate.of(1970, 1, 1).atStartOfDay()).build();
        ItemDto item = nameResult.nextBooking(nextBooking).requestId(1L).build();
        BookingDto.BookingDtoBuilder itemResult = idResult.item(item);
        BookingDto buildResult = itemResult.start(LocalDate.of(1970, 1, 1).atStartOfDay())
                .status(BookingStatus.WAITING)
                .build();
        when(bookingService.getById(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(buildResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/bookings/{bookingId}", 1L)
                .header("X-Sharer-User-Id", "42");

        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"start\":[1970,1,1,0,0],\"end\":[1970,1,1,0,0],\"status\":\"WAITING\",\"booker\":{\"id\":1,\"email\":\"jane"
                                        + ".doe@example.org\",\"name\":\"Name\"},\"item\":{\"id\":1,\"name\":\"Name\",\"description\":\"The characteristics of"
                                        + " someone or something\",\"available\":true,\"lastBooking\":{\"id\":1,\"bookerId\":1,\"startDate\":[1970,1,1,0,0"
                                        + "],\"endDate\":[1970,1,1,0,0]},\"nextBooking\":{\"id\":1,\"bookerId\":1,\"startDate\":[1970,1,1,0,0],\"endDate\":"
                                        + "[1970,1,1,0,0]},\"comments\":[],\"requestId\":1}}"));
    }

    @Test
    void testGetAll() throws Exception {
        when(bookingService.getAll(Mockito.<Long>any(), Mockito.<BookingState>any(), anyBoolean(), Mockito.<Pageable>any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/bookings");
        MockHttpServletRequestBuilder paramResult = getResult.param("from", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1))
                .param("state", "")
                .header("X-Sharer-User-Id", "42");

        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGetAllOwner() throws Exception {
        when(bookingService.getAll(Mockito.<Long>any(), Mockito.<BookingState>any(), anyBoolean(), Mockito.<Pageable>any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/bookings/owner");
        MockHttpServletRequestBuilder paramResult = getResult.param("from", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1))
                .param("state", "")
                .header("X-Sharer-User-Id", "42");

        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}
