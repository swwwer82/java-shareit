package ru.practicum.shareit.booking.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.util.BookingState;
import ru.practicum.shareit.booking.util.BookingStatus;
import ru.practicum.shareit.exception.exeption.NotFoundException;
import ru.practicum.shareit.exception.exeption.InvalidRequestException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@ContextConfiguration(classes = {BookingServiceImpl.class})
@ExtendWith(SpringExtension.class)
class BookingServiceImplTest {
    @MockBean
    private BookingMapper bookingMapper;

    @MockBean
    private BookingRepository bookingRepository;

    @Autowired
    private BookingServiceImpl bookingServiceImpl;

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserService userService;

    @Test
    void testCreate() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);

        User booker = new User();
        booker.setEmail("jane.doe@example.org");
        booker.setId(1L);
        booker.setName("Name");

        User owner = new User();
        owner.setEmail("jane.doe@example.org");
        owner.setId(1L);
        owner.setName("Name");

        User author = new User();
        author.setEmail("jane.doe@example.org");
        author.setId(1L);
        author.setName("Name");

        Request request = new Request();
        request.setAuthor(author);
        request.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(owner);
        item.setRequest(request);

        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setId(1L);
        booking.setItem(item);
        booking.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setStatus(BookingStatus.WAITING);
        when(bookingMapper.toBooking(Mockito.<Long>any(), Mockito.<BookingCreateDto>any())).thenReturn(booking);

        User owner2 = new User();
        owner2.setEmail("jane.doe@example.org");
        owner2.setId(1L);
        owner2.setName("Name");

        User author2 = new User();
        author2.setEmail("jane.doe@example.org");
        author2.setId(1L);
        author2.setName("Name");

        Request request2 = new Request();
        request2.setAuthor(author2);
        request2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request2.setDescription("The characteristics of someone or something");
        request2.setId(1L);

        Item item2 = new Item();
        item2.setAvailable(true);
        item2.setDescription("The characteristics of someone or something");
        item2.setId(1L);
        item2.setName("Name");
        item2.setOwner(owner2);
        item2.setRequest(request2);
        when(itemService.validationFindItemById(Mockito.<Long>any())).thenReturn(item2);

        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setEnd(LocalDate.of(1970, 1, 1).atStartOfDay());
        bookingCreateDto.setItemId(1L);
        bookingCreateDto.setStart(LocalDate.of(1970, 1, 1).atStartOfDay());

        assertThrows(InvalidRequestException.class, () -> bookingServiceImpl.create(1L, bookingCreateDto));
        verify(bookingMapper).toBooking(Mockito.<Long>any(), Mockito.<BookingCreateDto>any());
        verify(itemService).validationFindItemById(Mockito.<Long>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
    }

    @Test
    void testCreate2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        when(itemService.validationFindItemById(Mockito.<Long>any()))
                .thenThrow(new InvalidRequestException("An error occurred"));

        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setEnd(LocalDate.of(1970, 1, 1).atStartOfDay());
        bookingCreateDto.setItemId(1L);
        bookingCreateDto.setStart(LocalDate.of(1970, 1, 1).atStartOfDay());

        assertThrows(InvalidRequestException.class, () -> bookingServiceImpl.create(1L, bookingCreateDto));
        verify(itemService).validationFindItemById(Mockito.<Long>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
    }

    @Test
    void testApproveBooking() {
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
        when(bookingMapper.toBookingDto(Mockito.<Booking>any())).thenReturn(buildResult);

        User booker2 = new User();
        booker2.setEmail("jane.doe@example.org");
        booker2.setId(1L);
        booker2.setName("Name");

        User owner = new User();
        owner.setEmail("jane.doe@example.org");
        owner.setId(1L);
        owner.setName("Name");

        User author = new User();
        author.setEmail("jane.doe@example.org");
        author.setId(1L);
        author.setName("Name");

        Request request = new Request();
        request.setAuthor(author);
        request.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);

        Item item2 = new Item();
        item2.setAvailable(true);
        item2.setDescription("The characteristics of someone or something");
        item2.setId(1L);
        item2.setName("Name");
        item2.setOwner(owner);
        item2.setRequest(request);

        Booking booking = new Booking();
        booking.setBooker(booker2);
        booking.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setId(1L);
        booking.setItem(item2);
        booking.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setStatus(BookingStatus.WAITING);
        Optional<Booking> ofResult = Optional.of(booking);
        when(bookingRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        User owner2 = new User();
        owner2.setEmail("jane.doe@example.org");
        owner2.setId(1L);
        owner2.setName("Name");

        User author2 = new User();
        author2.setEmail("jane.doe@example.org");
        author2.setId(1L);
        author2.setName("Name");

        Request request2 = new Request();
        request2.setAuthor(author2);
        request2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request2.setDescription("The characteristics of someone or something");
        request2.setId(1L);

        Item item3 = new Item();
        item3.setAvailable(true);
        item3.setDescription("The characteristics of someone or something");
        item3.setId(1L);
        item3.setName("Name");
        item3.setOwner(owner2);
        item3.setRequest(request2);
        when(itemService.validationOwnerUserById(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(item3);

        bookingServiceImpl.approveBooking(1L, 1L, true);

        verify(bookingRepository).findById(Mockito.<Long>any());
        verify(bookingMapper).toBookingDto(Mockito.<Booking>any());
        verify(itemService).validationOwnerUserById(Mockito.<Long>any(), Mockito.<Long>any());
    }

    @Test
    void testApproveBooking2() {
        // Arrange
        User booker = new User();
        booker.setEmail("jane.doe@example.org");
        booker.setId(1L);
        booker.setName("Name");

        User owner = new User();
        owner.setEmail("jane.doe@example.org");
        owner.setId(1L);
        owner.setName("Name");

        User author = new User();
        author.setEmail("jane.doe@example.org");
        author.setId(1L);
        author.setName("Name");

        Request request = new Request();
        request.setAuthor(author);
        request.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(owner);
        item.setRequest(request);

        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setId(1L);
        booking.setItem(item);
        booking.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setStatus(BookingStatus.WAITING);
        Optional<Booking> ofResult = Optional.of(booking);
        when(bookingRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(itemService.validationOwnerUserById(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenThrow(new InvalidRequestException("An error occurred"));

        assertThrows(InvalidRequestException.class, () -> bookingServiceImpl.approveBooking(1L, 1L, true));
        verify(bookingRepository).findById(Mockito.<Long>any());
        verify(itemService).validationOwnerUserById(Mockito.<Long>any(), Mockito.<Long>any());
    }

    @Test
    void testApproveBooking3() {
        User booker = new User();
        booker.setEmail("jane.doe@example.org");
        booker.setId(1L);
        booker.setName("Name");

        User owner = new User();
        owner.setEmail("jane.doe@example.org");
        owner.setId(1L);
        owner.setName("Name");

        User author = new User();
        author.setEmail("jane.doe@example.org");
        author.setId(1L);
        author.setName("Name");

        Request request = new Request();
        request.setAuthor(author);
        request.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(owner);
        item.setRequest(request);

        User owner2 = new User();
        owner2.setEmail("jane.doe@example.org");
        owner2.setId(1L);
        owner2.setName("Name");

        User author2 = new User();
        author2.setEmail("jane.doe@example.org");
        author2.setId(1L);
        author2.setName("Name");

        Request request2 = new Request();
        request2.setAuthor(author2);
        request2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request2.setDescription("The characteristics of someone or something");
        request2.setId(1L);

        Item item2 = new Item();
        item2.setAvailable(true);
        item2.setDescription("The characteristics of someone or something");
        item2.setId(1L);
        item2.setName("Name");
        item2.setOwner(owner2);
        item2.setRequest(request2);
        Booking booking = mock(Booking.class);
        when(booking.getStatus()).thenThrow(new InvalidRequestException("An error occurred"));
        when(booking.getItem()).thenReturn(item2);
        doNothing().when(booking).setBooker(Mockito.<User>any());
        doNothing().when(booking).setEndDate(Mockito.<LocalDateTime>any());
        doNothing().when(booking).setId(Mockito.<Long>any());
        doNothing().when(booking).setItem(Mockito.<Item>any());
        doNothing().when(booking).setStartDate(Mockito.<LocalDateTime>any());
        doNothing().when(booking).setStatus(Mockito.<BookingStatus>any());
        booking.setBooker(booker);
        booking.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setId(1L);
        booking.setItem(item);
        booking.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setStatus(BookingStatus.WAITING);
        Optional<Booking> ofResult = Optional.of(booking);
        when(bookingRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        User owner3 = new User();
        owner3.setEmail("jane.doe@example.org");
        owner3.setId(1L);
        owner3.setName("Name");

        User author3 = new User();
        author3.setEmail("jane.doe@example.org");
        author3.setId(1L);
        author3.setName("Name");

        Request request3 = new Request();
        request3.setAuthor(author3);
        request3.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request3.setDescription("The characteristics of someone or something");
        request3.setId(1L);

        Item item3 = new Item();
        item3.setAvailable(true);
        item3.setDescription("The characteristics of someone or something");
        item3.setId(1L);
        item3.setName("Name");
        item3.setOwner(owner3);
        item3.setRequest(request3);
        when(itemService.validationOwnerUserById(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(item3);

        assertThrows(InvalidRequestException.class, () -> bookingServiceImpl.approveBooking(1L, 1L, true));
        verify(bookingRepository).findById(Mockito.<Long>any());
        verify(booking).getItem();
        verify(booking).getStatus();
        verify(booking).setBooker(Mockito.<User>any());
        verify(booking).setEndDate(Mockito.<LocalDateTime>any());
        verify(booking).setId(Mockito.<Long>any());
        verify(booking).setItem(Mockito.<Item>any());
        verify(booking).setStartDate(Mockito.<LocalDateTime>any());
        verify(booking).setStatus(eq(BookingStatus.WAITING));
        verify(itemService).validationOwnerUserById(Mockito.<Long>any(), Mockito.<Long>any());
    }

    @Test
    void testGetById() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
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
        when(bookingMapper.toBookingDto(Mockito.<Booking>any())).thenReturn(buildResult);

        User booker2 = new User();
        booker2.setEmail("jane.doe@example.org");
        booker2.setId(1L);
        booker2.setName("Name");

        User owner = new User();
        owner.setEmail("jane.doe@example.org");
        owner.setId(1L);
        owner.setName("Name");

        User author = new User();
        author.setEmail("jane.doe@example.org");
        author.setId(1L);
        author.setName("Name");

        Request request = new Request();
        request.setAuthor(author);
        request.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);

        Item item2 = new Item();
        item2.setAvailable(true);
        item2.setDescription("The characteristics of someone or something");
        item2.setId(1L);
        item2.setName("Name");
        item2.setOwner(owner);
        item2.setRequest(request);

        Booking booking = new Booking();
        booking.setBooker(booker2);
        booking.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setId(1L);
        booking.setItem(item2);
        booking.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setStatus(BookingStatus.WAITING);
        Optional<Booking> ofResult = Optional.of(booking);
        when(bookingRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        bookingServiceImpl.getById(1L, 1L);

        verify(bookingRepository).findById(Mockito.<Long>any());
        verify(bookingMapper).toBookingDto(Mockito.<Booking>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
    }

    @Test
    void testGetById2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        when(bookingMapper.toBookingDto(Mockito.<Booking>any()))
                .thenThrow(new InvalidRequestException("An error occurred"));

        User booker = new User();
        booker.setEmail("jane.doe@example.org");
        booker.setId(1L);
        booker.setName("Name");

        User owner = new User();
        owner.setEmail("jane.doe@example.org");
        owner.setId(1L);
        owner.setName("Name");

        User author = new User();
        author.setEmail("jane.doe@example.org");
        author.setId(1L);
        author.setName("Name");

        Request request = new Request();
        request.setAuthor(author);
        request.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(owner);
        item.setRequest(request);

        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setId(1L);
        booking.setItem(item);
        booking.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setStatus(BookingStatus.WAITING);
        Optional<Booking> ofResult = Optional.of(booking);
        when(bookingRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        assertThrows(InvalidRequestException.class, () -> bookingServiceImpl.getById(1L, 1L));
        verify(bookingRepository).findById(Mockito.<Long>any());
        verify(bookingMapper).toBookingDto(Mockito.<Booking>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
    }

    @Test
    void testGetAll() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        ArrayList<BookingDto> bookingDtoList = new ArrayList<>();
        when(bookingMapper.toBookingDto(Mockito.<List<Booking>>any())).thenReturn(bookingDtoList);
        when(bookingRepository.findAllByItem_Owner_idOrderByStartDateDesc(Mockito.<Long>any(), Mockito.<Pageable>any()))
                .thenReturn(new ArrayList<>());

        List<BookingDto> actualAll = bookingServiceImpl.getAll(1L, BookingState.ALL, true, null);

        verify(bookingMapper).toBookingDto(Mockito.<List<Booking>>any());
        verify(bookingRepository).findAllByItem_Owner_idOrderByStartDateDesc(Mockito.<Long>any(), Mockito.<Pageable>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
        assertTrue(actualAll.isEmpty());
        assertSame(bookingDtoList, actualAll);
    }

    @Test
    void testGetAll2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        when(bookingRepository.findAllByItem_Owner_idOrderByStartDateDesc(Mockito.<Long>any(), Mockito.<Pageable>any()))
                .thenThrow(new InvalidRequestException("An error occurred"));

        assertThrows(InvalidRequestException.class, () -> bookingServiceImpl.getAll(1L, BookingState.ALL, true, null));
        verify(bookingRepository).findAllByItem_Owner_idOrderByStartDateDesc(Mockito.<Long>any(), Mockito.<Pageable>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
    }

    @Test
    void testGetAll3() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        ArrayList<BookingDto> bookingDtoList = new ArrayList<>();
        when(bookingMapper.toBookingDto(Mockito.<List<Booking>>any())).thenReturn(bookingDtoList);
        when(bookingRepository.findAllByStartDateBeforeAndEndDateAfterAndItem_Owner_idOrderByStartDateDesc(
                Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any(), Mockito.<Long>any(), Mockito.<Pageable>any()))
                .thenReturn(new ArrayList<>());

        List<BookingDto> actualAll = bookingServiceImpl.getAll(1L, BookingState.CURRENT, true, null);

        verify(bookingMapper).toBookingDto(Mockito.<List<Booking>>any());
        verify(bookingRepository).findAllByStartDateBeforeAndEndDateAfterAndItem_Owner_idOrderByStartDateDesc(
                Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any(), Mockito.<Long>any(), Mockito.<Pageable>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
        assertTrue(actualAll.isEmpty());
        assertSame(bookingDtoList, actualAll);
    }

    @Test
    void testGetAll4() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        ArrayList<BookingDto> bookingDtoList = new ArrayList<>();
        when(bookingMapper.toBookingDto(Mockito.<List<Booking>>any())).thenReturn(bookingDtoList);
        when(bookingRepository.findAllByEndDateBeforeAndItem_Owner_idOrderByStartDateDesc(Mockito.<LocalDateTime>any(),
                Mockito.<Long>any(), Mockito.<Pageable>any())).thenReturn(new ArrayList<>());

        List<BookingDto> actualAll = bookingServiceImpl.getAll(1L, BookingState.PAST, true, null);

        verify(bookingMapper).toBookingDto(Mockito.<List<Booking>>any());
        verify(bookingRepository).findAllByEndDateBeforeAndItem_Owner_idOrderByStartDateDesc(Mockito.<LocalDateTime>any(),
                Mockito.<Long>any(), Mockito.<Pageable>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
        assertTrue(actualAll.isEmpty());
        assertSame(bookingDtoList, actualAll);
    }

    @Test
    void testGetAll5() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        ArrayList<BookingDto> bookingDtoList = new ArrayList<>();
        when(bookingMapper.toBookingDto(Mockito.<List<Booking>>any())).thenReturn(bookingDtoList);
        when(bookingRepository.findAllByStartDateAfterAndItem_Owner_idOrderByStartDateDesc(Mockito.<LocalDateTime>any(),
                Mockito.<Long>any(), Mockito.<Pageable>any())).thenReturn(new ArrayList<>());

        List<BookingDto> actualAll = bookingServiceImpl.getAll(1L, BookingState.FUTURE, true, null);

        verify(bookingMapper).toBookingDto(Mockito.<List<Booking>>any());
        verify(bookingRepository).findAllByStartDateAfterAndItem_Owner_idOrderByStartDateDesc(Mockito.<LocalDateTime>any(),
                Mockito.<Long>any(), Mockito.<Pageable>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
        assertTrue(actualAll.isEmpty());
        assertSame(bookingDtoList, actualAll);
    }

    @Test
    void testGetAll6() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        ArrayList<BookingDto> bookingDtoList = new ArrayList<>();
        when(bookingMapper.toBookingDto(Mockito.<List<Booking>>any())).thenReturn(bookingDtoList);
        when(bookingRepository.findAllByStartDateAfterAndBooker_idOrderByStartDateDesc(Mockito.<LocalDateTime>any(),
                Mockito.<Long>any(), Mockito.<Pageable>any())).thenReturn(new ArrayList<>());

        List<BookingDto> actualAll = bookingServiceImpl.getAll(1L, BookingState.FUTURE, false, null);

        verify(bookingMapper).toBookingDto(Mockito.<List<Booking>>any());
        verify(bookingRepository).findAllByStartDateAfterAndBooker_idOrderByStartDateDesc(Mockito.<LocalDateTime>any(),
                Mockito.<Long>any(), Mockito.<Pageable>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
        assertTrue(actualAll.isEmpty());
        assertSame(bookingDtoList, actualAll);
    }

    @Test
    void testValidateFindBookingById() {
        User booker = new User();
        booker.setEmail("jane.doe@example.org");
        booker.setId(1L);
        booker.setName("Name");

        User owner = new User();
        owner.setEmail("jane.doe@example.org");
        owner.setId(1L);
        owner.setName("Name");

        User author = new User();
        author.setEmail("jane.doe@example.org");
        author.setId(1L);
        author.setName("Name");

        Request request = new Request();
        request.setAuthor(author);
        request.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(owner);
        item.setRequest(request);

        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setId(1L);
        booking.setItem(item);
        booking.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setStatus(BookingStatus.WAITING);
        Optional<Booking> ofResult = Optional.of(booking);
        when(bookingRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Booking actualValidateFindBookingByIdResult = bookingServiceImpl.validateFindBookingById(1L);

        verify(bookingRepository).findById(Mockito.<Long>any());
        assertSame(booking, actualValidateFindBookingByIdResult);
    }

    @Test
    void testValidateFindBookingById2() {
        Optional<Booking> emptyResult = Optional.empty();
        when(bookingRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        assertThrows(NotFoundException.class, () -> bookingServiceImpl.validateFindBookingById(1L));
        verify(bookingRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testValidateFindBookingById3() {
        when(bookingRepository.findById(Mockito.<Long>any())).thenThrow(new InvalidRequestException("An error occurred"));

        assertThrows(InvalidRequestException.class, () -> bookingServiceImpl.validateFindBookingById(1L));
        verify(bookingRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testValidationCreateBooking() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        User owner = new User();
        owner.setEmail("jane.doe@example.org");
        owner.setId(1L);
        owner.setName("Name");

        User author = new User();
        author.setEmail("jane.doe@example.org");
        author.setId(1L);
        author.setName("Name");

        Request request = new Request();
        request.setAuthor(author);
        request.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(owner);
        item.setRequest(request);

        User booker = new User();
        booker.setEmail("jane.doe@example.org");
        booker.setId(1L);
        booker.setName("Name");

        User owner2 = new User();
        owner2.setEmail("jane.doe@example.org");
        owner2.setId(1L);
        owner2.setName("Name");

        User author2 = new User();
        author2.setEmail("jane.doe@example.org");
        author2.setId(1L);
        author2.setName("Name");

        Request request2 = new Request();
        request2.setAuthor(author2);
        request2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request2.setDescription("The characteristics of someone or something");
        request2.setId(1L);

        Item item2 = new Item();
        item2.setAvailable(true);
        item2.setDescription("The characteristics of someone or something");
        item2.setId(1L);
        item2.setName("Name");
        item2.setOwner(owner2);
        item2.setRequest(request2);

        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setId(1L);
        booking.setItem(item2);
        booking.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setStatus(BookingStatus.WAITING);

        assertThrows(InvalidRequestException.class, () -> bookingServiceImpl.validationCreateBooking(user, item, booking));
    }

    @Test
    void testValidationCreateBooking2() {
        User user = mock(User.class);
        doNothing().when(user).setEmail(Mockito.<String>any());
        doNothing().when(user).setId(Mockito.<Long>any());
        doNothing().when(user).setName(Mockito.<String>any());
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        User owner = new User();
        owner.setEmail("jane.doe@example.org");
        owner.setId(1L);
        owner.setName("Name");

        User author = new User();
        author.setEmail("jane.doe@example.org");
        author.setId(1L);
        author.setName("Name");

        Request request = new Request();
        request.setAuthor(author);
        request.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        Item item = mock(Item.class);
        when(item.getAvailable()).thenReturn(false);
        doNothing().when(item).setAvailable(Mockito.<Boolean>any());
        doNothing().when(item).setDescription(Mockito.<String>any());
        doNothing().when(item).setId(Mockito.<Long>any());
        doNothing().when(item).setName(Mockito.<String>any());
        doNothing().when(item).setOwner(Mockito.<User>any());
        doNothing().when(item).setRequest(Mockito.<Request>any());
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(owner);
        item.setRequest(request);

        User booker = new User();
        booker.setEmail("jane.doe@example.org");
        booker.setId(1L);
        booker.setName("Name");

        User owner2 = new User();
        owner2.setEmail("jane.doe@example.org");
        owner2.setId(1L);
        owner2.setName("Name");

        User author2 = new User();
        author2.setEmail("jane.doe@example.org");
        author2.setId(1L);
        author2.setName("Name");

        Request request2 = new Request();
        request2.setAuthor(author2);
        request2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request2.setDescription("The characteristics of someone or something");
        request2.setId(1L);

        Item item2 = new Item();
        item2.setAvailable(true);
        item2.setDescription("The characteristics of someone or something");
        item2.setId(1L);
        item2.setName("Name");
        item2.setOwner(owner2);
        item2.setRequest(request2);

        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setId(1L);
        booking.setItem(item2);
        booking.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setStatus(BookingStatus.WAITING);

        assertThrows(InvalidRequestException.class, () -> bookingServiceImpl.validationCreateBooking(user, item, booking));
        verify(item).getAvailable();
        verify(item).setAvailable(Mockito.<Boolean>any());
        verify(item).setDescription(eq("The characteristics of someone or something"));
        verify(item).setId(Mockito.<Long>any());
        verify(item).setName(eq("Name"));
        verify(item).setOwner(Mockito.<User>any());
        verify(item).setRequest(Mockito.<Request>any());
        verify(user).setEmail(eq("jane.doe@example.org"));
        verify(user).setId(Mockito.<Long>any());
        verify(user).setName(eq("Name"));
    }
}
