package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.util.BookingStatus;
import ru.practicum.shareit.exception.exeption.NotFoundException;
import ru.practicum.shareit.exception.exeption.NotValidRequestException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ItemServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ItemServiceImplTest {
    @MockBean
    private BookingMapper bookingMapper;

    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private CommentMapper commentMapper;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private ItemMapper itemMapper;

    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private ItemServiceImpl itemServiceImpl;

    @MockBean
    private RequestService requestService;

    @MockBean
    private UserService userService;

    @Test
    void testGet() {
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
        Optional<Item> ofResult = Optional.of(item);
        when(itemRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
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
        when(itemMapper.toItemDto(Mockito.any())).thenReturn(buildResult);
        when(bookingRepository.findAllByItem_idAndStatusAndItem_owner_id(Mockito.<Long>any(), Mockito.any(),
                Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(commentMapper.toCommentDto(Mockito.<List<Comment>>any())).thenReturn(new ArrayList<>());
        when(commentRepository.findAllByItem_id(Mockito.<Long>any())).thenReturn(new ArrayList<>());

        ItemDto actualGetResult = itemServiceImpl.get(1L, 1L);

        verify(itemRepository).findById(Mockito.<Long>any());
        verify(bookingRepository).findAllByItem_idAndStatusAndItem_owner_id(Mockito.<Long>any(), eq(BookingStatus.APPROVED),
                Mockito.<Long>any());
        verify(commentMapper).toCommentDto(Mockito.<List<Comment>>any());
        verify(itemMapper).toItemDto(Mockito.any());
        verify(commentRepository).findAllByItem_id(Mockito.<Long>any());
        BookingItemDto lastBooking2 = actualGetResult.getLastBooking();
        LocalTime expectedToLocalTimeResult = lastBooking2.getStartDate().toLocalTime();
        assertSame(expectedToLocalTimeResult, lastBooking2.getEndDate().toLocalTime());
    }

    @Test
    void testGet2() {
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
        Optional<Item> ofResult = Optional.of(item);
        when(itemRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
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
        when(itemMapper.toItemDto(Mockito.any())).thenReturn(buildResult);
        when(commentRepository.findAllByItem_id(Mockito.<Long>any())).thenThrow(new NotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(NotFoundException.class, () -> itemServiceImpl.get(1L, 1L));
        verify(itemRepository).findById(Mockito.<Long>any());
        verify(itemMapper).toItemDto(Mockito.any());
        verify(commentRepository).findAllByItem_id(Mockito.<Long>any());
    }

    @Test
    void testGet3() {
        // Arrange
        Optional<Item> emptyResult = Optional.empty();
        when(itemRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> itemServiceImpl.get(1L, 1L));
        verify(itemRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testGetAll() {
        when(itemRepository.findAllByOwnerId(Mockito.<Long>any(), Mockito.any())).thenReturn(new ArrayList<>());
        when(bookingRepository.findAllByItem_idInAndStatusAndItem_owner_id(Mockito.any(),
                Mockito.any(), Mockito.<Long>any())).thenReturn(new ArrayList<>());

        List<ItemDto> actualAll = itemServiceImpl.getAll(1L, null);

        verify(bookingRepository).findAllByItem_idInAndStatusAndItem_owner_id(Mockito.any(),
                eq(BookingStatus.APPROVED), Mockito.<Long>any());
        verify(itemRepository).findAllByOwnerId(Mockito.<Long>any(), Mockito.any());
        assertTrue(actualAll.isEmpty());
    }

    @Test
    void testGetAll2() {
        when(itemRepository.findAllByOwnerId(Mockito.<Long>any(), Mockito.any())).thenReturn(new ArrayList<>());
        when(bookingRepository.findAllByItem_idInAndStatusAndItem_owner_id(Mockito.any(),
                Mockito.any(), Mockito.<Long>any())).thenThrow(new NotFoundException("An error occurred"));

        assertThrows(NotFoundException.class, () -> itemServiceImpl.getAll(1L, null));
        verify(bookingRepository).findAllByItem_idInAndStatusAndItem_owner_id(Mockito.any(),
                eq(BookingStatus.APPROVED), Mockito.<Long>any());
        verify(itemRepository).findAllByOwnerId(Mockito.<Long>any(), Mockito.any());
    }

    @Test
    void testGetAll3() {
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

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(itemRepository.findAllByOwnerId(Mockito.<Long>any(), Mockito.any())).thenReturn(itemList);
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
        when(itemMapper.toItemDto(Mockito.any())).thenReturn(buildResult);
        when(bookingRepository.findAllByItem_idInAndStatusAndItem_owner_id(Mockito.any(),
                Mockito.any(), Mockito.<Long>any())).thenReturn(new ArrayList<>());

        List<ItemDto> actualAll = itemServiceImpl.getAll(1L, null);

        verify(bookingRepository).findAllByItem_idInAndStatusAndItem_owner_id(Mockito.any(),
                eq(BookingStatus.APPROVED), Mockito.<Long>any());
        verify(itemMapper).toItemDto(Mockito.any());
        verify(itemRepository).findAllByOwnerId(Mockito.<Long>any(), Mockito.any());
        assertEquals(1, actualAll.size());
    }

    @Test
    void testCreate() {
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
        when(itemRepository.save(Mockito.any())).thenReturn(item);

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
        when(itemMapper.toItemDto(Mockito.any())).thenReturn(buildResult);
        when(itemMapper.toItem(Mockito.<ItemCreateDto>any())).thenReturn(item2);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);

        User author3 = new User();
        author3.setEmail("jane.doe@example.org");
        author3.setId(1L);
        author3.setName("Name");

        Request request3 = new Request();
        request3.setAuthor(author3);
        request3.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request3.setDescription("The characteristics of someone or something");
        request3.setId(1L);
        when(requestService.validateFindRequestById(Mockito.<Long>any())).thenReturn(request3);

        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setAvailable(true);
        itemCreateDto.setDescription("The characteristics of someone or something");
        itemCreateDto.setName("Name");
        itemCreateDto.setRequestId(1L);

        ItemDto actualCreateResult = itemServiceImpl.create(1L, itemCreateDto);

        verify(itemRepository).save(Mockito.any());
        verify(itemMapper).toItem(Mockito.<ItemCreateDto>any());
        verify(itemMapper).toItemDto(Mockito.any());
        verify(requestService).validateFindRequestById(Mockito.<Long>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
        BookingItemDto lastBooking2 = actualCreateResult.getLastBooking();
        LocalTime expectedToLocalTimeResult = lastBooking2.getStartDate().toLocalTime();
        assertSame(expectedToLocalTimeResult, lastBooking2.getEndDate().toLocalTime());
    }

    @Test
    void testCreate2() {
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
        when(itemMapper.toItem(Mockito.<ItemCreateDto>any())).thenReturn(item);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        when(requestService.validateFindRequestById(Mockito.<Long>any()))
                .thenThrow(new NotFoundException("An error occurred"));

        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setAvailable(true);
        itemCreateDto.setDescription("The characteristics of someone or something");
        itemCreateDto.setName("Name");
        itemCreateDto.setRequestId(1L);

        assertThrows(NotFoundException.class, () -> itemServiceImpl.create(1L, itemCreateDto));
        verify(itemMapper).toItem(Mockito.<ItemCreateDto>any());
        verify(requestService).validateFindRequestById(Mockito.<Long>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
    }

    @Test
    void testUpdate() {
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
        Optional<Item> ofResult = Optional.of(item);
        when(itemRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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
        when(itemMapper.toItemDto(Mockito.any())).thenReturn(buildResult);
        when(itemMapper.toItem(Mockito.<ItemUpdateDto>any())).thenReturn(item2);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);

        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setAvailable(true);
        itemUpdateDto.setDescription("The characteristics of someone or something");
        itemUpdateDto.setName("Name");

        ItemDto actualUpdateResult = itemServiceImpl.update(1L, 1L, itemUpdateDto);

        verify(itemRepository).findById(Mockito.<Long>any());
        verify(itemMapper).toItem(Mockito.<ItemUpdateDto>any());
        verify(itemMapper).toItemDto(Mockito.any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
        BookingItemDto lastBooking2 = actualUpdateResult.getLastBooking();
        LocalTime expectedToLocalTimeResult = lastBooking2.getStartDate().toLocalTime();
        assertSame(expectedToLocalTimeResult, lastBooking2.getEndDate().toLocalTime());
    }

    @Test
    void testUpdate2() {
        when(userService.validationFindUserById(Mockito.<Long>any())).thenThrow(new NotFoundException("An error occurred"));

        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setAvailable(true);
        itemUpdateDto.setDescription("The characteristics of someone or something");
        itemUpdateDto.setName("Name");

        assertThrows(NotFoundException.class, () -> itemServiceImpl.update(1L, 1L, itemUpdateDto));
        verify(userService).validationFindUserById(Mockito.<Long>any());
    }

    @Test
    void testUpdate3() {
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
        User user = mock(User.class);
        when(user.getId()).thenReturn(-1L);
        doNothing().when(user).setEmail(Mockito.any());
        doNothing().when(user).setId(Mockito.<Long>any());
        doNothing().when(user).setName(Mockito.any());
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        Item item = mock(Item.class);
        when(item.getOwner()).thenReturn(user);
        doNothing().when(item).setAvailable(Mockito.<Boolean>any());
        doNothing().when(item).setDescription(Mockito.any());
        doNothing().when(item).setId(Mockito.<Long>any());
        doNothing().when(item).setName(Mockito.any());
        doNothing().when(item).setOwner(Mockito.any());
        doNothing().when(item).setRequest(Mockito.any());
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(owner);
        item.setRequest(request);
        Optional<Item> ofResult = Optional.of(item);
        when(itemRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user2);

        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setAvailable(true);
        itemUpdateDto.setDescription("The characteristics of someone or something");
        itemUpdateDto.setName("Name");

        assertThrows(NotFoundException.class, () -> itemServiceImpl.update(1L, 1L, itemUpdateDto));
        verify(itemRepository).findById(Mockito.<Long>any());
        verify(item).getOwner();
        verify(item).setAvailable(Mockito.<Boolean>any());
        verify(item).setDescription(eq("The characteristics of someone or something"));
        verify(item).setId(Mockito.<Long>any());
        verify(item).setName(eq("Name"));
        verify(item).setOwner(Mockito.any());
        verify(item).setRequest(Mockito.any());
        verify(user).getId();
        verify(user).setEmail(eq("jane.doe@example.org"));
        verify(user).setId(Mockito.<Long>any());
        verify(user).setName(eq("Name"));
        verify(userService).validationFindUserById(Mockito.<Long>any());
    }

    @Test
    void testUpdate4() {
        Optional<Item> emptyResult = Optional.empty();
        when(itemRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);

        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setAvailable(true);
        itemUpdateDto.setDescription("The characteristics of someone or something");
        itemUpdateDto.setName("Name");

        assertThrows(NotFoundException.class, () -> itemServiceImpl.update(1L, 1L, itemUpdateDto));
        verify(itemRepository).findById(Mockito.<Long>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
    }

    @Test
    void testSearch() {
        when(itemRepository.search(Mockito.any(), Mockito.any())).thenReturn(new ArrayList<>());

        List<ItemDto> actualSearchResult = itemServiceImpl.search("Text", null);

        verify(itemRepository).search(eq("Text"), Mockito.any());
        assertTrue(actualSearchResult.isEmpty());
    }

    @Test
    void testValidationFindItemById() {
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
        Optional<Item> ofResult = Optional.of(item);
        when(itemRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Item actualValidationFindItemByIdResult = itemServiceImpl.validationFindItemById(1L);

        verify(itemRepository).findById(Mockito.<Long>any());
        assertSame(item, actualValidationFindItemByIdResult);
    }

    @Test
    void testValidationFindItemById2() {
        Optional<Item> emptyResult = Optional.empty();
        when(itemRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        assertThrows(NotFoundException.class, () -> itemServiceImpl.validationFindItemById(1L));
        verify(itemRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testValidationFindItemById3() {
        when(itemRepository.findById(Mockito.<Long>any())).thenThrow(new NotFoundException("An error occurred"));

        assertThrows(NotFoundException.class, () -> itemServiceImpl.validationFindItemById(1L));
        verify(itemRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testValidationOwnerUserById() {
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
        Optional<Item> ofResult = Optional.of(item);
        when(itemRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Item actualValidationOwnerUserByIdResult = itemServiceImpl.validationOwnerUserById(1L, 1L);

        verify(itemRepository).findById(Mockito.<Long>any());
        assertSame(item, actualValidationOwnerUserByIdResult);
    }

    @Test
    void testValidationOwnerUserById2() {
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
        User user = mock(User.class);
        when(user.getId()).thenReturn(-1L);
        doNothing().when(user).setEmail(Mockito.any());
        doNothing().when(user).setId(Mockito.<Long>any());
        doNothing().when(user).setName(Mockito.any());
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        Item item = mock(Item.class);
        when(item.getOwner()).thenReturn(user);
        doNothing().when(item).setAvailable(Mockito.<Boolean>any());
        doNothing().when(item).setDescription(Mockito.any());
        doNothing().when(item).setId(Mockito.<Long>any());
        doNothing().when(item).setName(Mockito.any());
        doNothing().when(item).setOwner(Mockito.any());
        doNothing().when(item).setRequest(Mockito.any());
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(owner);
        item.setRequest(request);
        Optional<Item> ofResult = Optional.of(item);
        when(itemRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        assertThrows(NotFoundException.class, () -> itemServiceImpl.validationOwnerUserById(1L, 1L));
        verify(itemRepository).findById(Mockito.<Long>any());
        verify(item).getOwner();
        verify(item).setAvailable(Mockito.<Boolean>any());
        verify(item).setDescription(eq("The characteristics of someone or something"));
        verify(item).setId(Mockito.<Long>any());
        verify(item).setName(eq("Name"));
        verify(item).setOwner(Mockito.any());
        verify(item).setRequest(Mockito.any());
        verify(user).getId();
        verify(user).setEmail(eq("jane.doe@example.org"));
        verify(user).setId(Mockito.<Long>any());
        verify(user).setName(eq("Name"));
    }

    @Test
    void testValidationOwnerUserById3() {
        Optional<Item> emptyResult = Optional.empty();
        when(itemRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        assertThrows(NotFoundException.class, () -> itemServiceImpl.validationOwnerUserById(1L, 1L));
        verify(itemRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testCreateComment() {
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
        Optional<Item> ofResult = Optional.of(item);
        when(itemRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        when(bookingRepository.findAllByEndDateBeforeAndBooker_idAndItem_idAndStatus(Mockito.any(),
                Mockito.<Long>any(), Mockito.<Long>any(), Mockito.any())).thenReturn(new ArrayList<>());

        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText("Text");

        assertThrows(NotValidRequestException.class, () -> itemServiceImpl.createComment(1L, 1L, commentCreateDto));
        verify(itemRepository).findById(Mockito.<Long>any());
        verify(bookingRepository).findAllByEndDateBeforeAndBooker_idAndItem_idAndStatus(Mockito.any(),
                Mockito.<Long>any(), Mockito.<Long>any(), eq(BookingStatus.APPROVED));
        verify(userService).validationFindUserById(Mockito.<Long>any());
    }

    @Test
    void testCreateComment2() {
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
        Optional<Item> ofResult = Optional.of(item);
        when(itemRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        when(bookingRepository.findAllByEndDateBeforeAndBooker_idAndItem_idAndStatus(Mockito.any(),
                Mockito.<Long>any(), Mockito.<Long>any(), Mockito.any()))
                .thenThrow(new NotFoundException("An error occurred"));

        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText("Text");

        assertThrows(NotFoundException.class, () -> itemServiceImpl.createComment(1L, 1L, commentCreateDto));
        verify(itemRepository).findById(Mockito.<Long>any());
        verify(bookingRepository).findAllByEndDateBeforeAndBooker_idAndItem_idAndStatus(Mockito.any(),
                Mockito.<Long>any(), Mockito.<Long>any(), eq(BookingStatus.APPROVED));
        verify(userService).validationFindUserById(Mockito.<Long>any());
    }

    @Test
    void testCreateComment3() {
        Optional<Item> emptyResult = Optional.empty();
        when(itemRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);

        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText("Text");

        assertThrows(NotFoundException.class, () -> itemServiceImpl.createComment(1L, 1L, commentCreateDto));
        verify(itemRepository).findById(Mockito.<Long>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
    }

    @Test
    void testCreateComment4() {
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
        Optional<Item> ofResult = Optional.of(item);
        when(itemRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);

        User booker = new User();
        booker.setEmail("jane.doe@example.org");
        booker.setId(1L);
        booker.setName("Объект не доступен");

        User owner2 = new User();
        owner2.setEmail("jane.doe@example.org");
        owner2.setId(1L);
        owner2.setName("Объект не доступен");

        User author2 = new User();
        author2.setEmail("jane.doe@example.org");
        author2.setId(1L);
        author2.setName("Объект не доступен");

        Request request2 = new Request();
        request2.setAuthor(author2);
        request2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request2.setDescription("The characteristics of someone or something");
        request2.setId(1L);

        Item item2 = new Item();
        item2.setAvailable(true);
        item2.setDescription("The characteristics of someone or something");
        item2.setId(1L);
        item2.setName("Объект не доступен");
        item2.setOwner(owner2);
        item2.setRequest(request2);

        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setId(1L);
        booking.setItem(item2);
        booking.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setStatus(BookingStatus.WAITING);

        ArrayList<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        when(bookingRepository.findAllByEndDateBeforeAndBooker_idAndItem_idAndStatus(Mockito.any(),
                Mockito.<Long>any(), Mockito.<Long>any(), Mockito.any())).thenReturn(bookingList);

        User author3 = new User();
        author3.setEmail("jane.doe@example.org");
        author3.setId(1L);
        author3.setName("Name");

        User owner3 = new User();
        owner3.setEmail("jane.doe@example.org");
        owner3.setId(1L);
        owner3.setName("Name");

        User author4 = new User();
        author4.setEmail("jane.doe@example.org");
        author4.setId(1L);
        author4.setName("Name");

        Request request3 = new Request();
        request3.setAuthor(author4);
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

        Comment comment = new Comment();
        comment.setAuthor(author3);
        comment.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        comment.setId(1L);
        comment.setItem(item3);
        comment.setText("Text");
        CommentDto.CommentDtoBuilder authorNameResult = CommentDto.builder().authorName("JaneDoe");
        CommentDto buildResult = authorNameResult.created(LocalDate.of(1970, 1, 1).atStartOfDay())
                .id(1L)
                .text("Text")
                .build();
        when(commentMapper.toCommentDto(Mockito.<Comment>any())).thenReturn(buildResult);
        when(commentMapper.toComment(Mockito.<Long>any(), Mockito.<Long>any(), Mockito.any()))
                .thenReturn(comment);

        User author5 = new User();
        author5.setEmail("jane.doe@example.org");
        author5.setId(1L);
        author5.setName("Name");

        User owner4 = new User();
        owner4.setEmail("jane.doe@example.org");
        owner4.setId(1L);
        owner4.setName("Name");

        User author6 = new User();
        author6.setEmail("jane.doe@example.org");
        author6.setId(1L);
        author6.setName("Name");

        Request request4 = new Request();
        request4.setAuthor(author6);
        request4.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request4.setDescription("The characteristics of someone or something");
        request4.setId(1L);

        Item item4 = new Item();
        item4.setAvailable(true);
        item4.setDescription("The characteristics of someone or something");
        item4.setId(1L);
        item4.setName("Name");
        item4.setOwner(owner4);
        item4.setRequest(request4);

        Comment comment2 = new Comment();
        comment2.setAuthor(author5);
        comment2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        comment2.setId(1L);
        comment2.setItem(item4);
        comment2.setText("Text");
        when(commentRepository.save(Mockito.any())).thenReturn(comment2);

        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText("Text");

        itemServiceImpl.createComment(1L, 1L, commentCreateDto);

        verify(itemRepository).findById(Mockito.<Long>any());
        verify(commentRepository).save(Mockito.any());
        verify(bookingRepository).findAllByEndDateBeforeAndBooker_idAndItem_idAndStatus(Mockito.any(),
                Mockito.<Long>any(), Mockito.<Long>any(), eq(BookingStatus.APPROVED));
        verify(commentMapper).toComment(Mockito.<Long>any(), Mockito.<Long>any(), Mockito.any());
        verify(commentMapper).toCommentDto(Mockito.<Comment>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
    }

    @Test
    void testCreateComment5() {
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
        Optional<Item> ofResult = Optional.of(item);
        when(itemRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);

        User booker = new User();
        booker.setEmail("jane.doe@example.org");
        booker.setId(1L);
        booker.setName("Объект не доступен");

        User owner2 = new User();
        owner2.setEmail("jane.doe@example.org");
        owner2.setId(1L);
        owner2.setName("Объект не доступен");

        User author2 = new User();
        author2.setEmail("jane.doe@example.org");
        author2.setId(1L);
        author2.setName("Объект не доступен");

        Request request2 = new Request();
        request2.setAuthor(author2);
        request2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request2.setDescription("The characteristics of someone or something");
        request2.setId(1L);

        Item item2 = new Item();
        item2.setAvailable(true);
        item2.setDescription("The characteristics of someone or something");
        item2.setId(1L);
        item2.setName("Объект не доступен");
        item2.setOwner(owner2);
        item2.setRequest(request2);

        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setId(1L);
        booking.setItem(item2);
        booking.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        booking.setStatus(BookingStatus.WAITING);

        ArrayList<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        when(bookingRepository.findAllByEndDateBeforeAndBooker_idAndItem_idAndStatus(Mockito.any(),
                Mockito.<Long>any(), Mockito.<Long>any(), Mockito.any())).thenReturn(bookingList);

        User author3 = new User();
        author3.setEmail("jane.doe@example.org");
        author3.setId(1L);
        author3.setName("Name");

        User owner3 = new User();
        owner3.setEmail("jane.doe@example.org");
        owner3.setId(1L);
        owner3.setName("Name");

        User author4 = new User();
        author4.setEmail("jane.doe@example.org");
        author4.setId(1L);
        author4.setName("Name");

        Request request3 = new Request();
        request3.setAuthor(author4);
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

        Comment comment = new Comment();
        comment.setAuthor(author3);
        comment.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        comment.setId(1L);
        comment.setItem(item3);
        comment.setText("Text");
        when(commentMapper.toCommentDto(Mockito.<Comment>any())).thenThrow(new NotFoundException("An error occurred"));
        when(commentMapper.toComment(Mockito.<Long>any(), Mockito.<Long>any(), Mockito.any()))
                .thenReturn(comment);

        User author5 = new User();
        author5.setEmail("jane.doe@example.org");
        author5.setId(1L);
        author5.setName("Name");

        User owner4 = new User();
        owner4.setEmail("jane.doe@example.org");
        owner4.setId(1L);
        owner4.setName("Name");

        User author6 = new User();
        author6.setEmail("jane.doe@example.org");
        author6.setId(1L);
        author6.setName("Name");

        Request request4 = new Request();
        request4.setAuthor(author6);
        request4.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request4.setDescription("The characteristics of someone or something");
        request4.setId(1L);

        Item item4 = new Item();
        item4.setAvailable(true);
        item4.setDescription("The characteristics of someone or something");
        item4.setId(1L);
        item4.setName("Name");
        item4.setOwner(owner4);
        item4.setRequest(request4);

        Comment comment2 = new Comment();
        comment2.setAuthor(author5);
        comment2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        comment2.setId(1L);
        comment2.setItem(item4);
        comment2.setText("Text");
        when(commentRepository.save(Mockito.any())).thenReturn(comment2);

        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText("Text");

        assertThrows(NotFoundException.class, () -> itemServiceImpl.createComment(1L, 1L, commentCreateDto));
        verify(itemRepository).findById(Mockito.<Long>any());
        verify(commentRepository).save(Mockito.any());
        verify(bookingRepository).findAllByEndDateBeforeAndBooker_idAndItem_idAndStatus(Mockito.any(),
                Mockito.<Long>any(), Mockito.<Long>any(), eq(BookingStatus.APPROVED));
        verify(commentMapper).toComment(Mockito.<Long>any(), Mockito.<Long>any(), Mockito.any());
        verify(commentMapper).toCommentDto(Mockito.<Comment>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
    }
}
