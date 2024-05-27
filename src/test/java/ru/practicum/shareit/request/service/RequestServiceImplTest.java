package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.exception.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {RequestServiceImpl.class})
@ExtendWith(SpringExtension.class)
class RequestServiceImplTest {
    @MockBean
    private ItemMapper itemMapper;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private RequestMapper requestMapper;

    @MockBean
    private RequestRepository requestRepository;

    @Autowired
    private RequestServiceImpl requestServiceImpl;

    @MockBean
    private UserService userService;

    @Test
    void testCreate() {
        User author = new User();
        author.setEmail("jane.doe@example.org");
        author.setId(1L);
        author.setName("Name");

        Request request = new Request();
        request.setAuthor(author);
        request.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        when(requestRepository.save(Mockito.any())).thenReturn(request);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);

        User author2 = new User();
        author2.setEmail("jane.doe@example.org");
        author2.setId(1L);
        author2.setName("Name");

        Request request2 = new Request();
        request2.setAuthor(author2);
        request2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request2.setDescription("The characteristics of someone or something");
        request2.setId(1L);
        RequestDto.RequestDtoBuilder builderResult = RequestDto.builder();
        RequestDto.RequestDtoBuilder idResult = builderResult.created(LocalDate.of(1970, 1, 1).atStartOfDay())
                .description("The characteristics of someone or something")
                .id(1L);
        RequestDto buildResult = idResult.items(new ArrayList<>()).build();
        when(requestMapper.toRequestDto(Mockito.any())).thenReturn(buildResult);
        when(requestMapper.toRequest(Mockito.<Long>any(), Mockito.any())).thenReturn(request2);

        RequestCreateDto requestCreateDto = new RequestCreateDto();
        requestCreateDto.setDescription("The characteristics of someone or something");

        requestServiceImpl.create(1L, requestCreateDto);

        verify(requestRepository).save(Mockito.any());
        verify(requestMapper).toRequest(Mockito.<Long>any(), Mockito.any());
        verify(requestMapper).toRequestDto(Mockito.any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
    }

    @Test
    void testCreate2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        when(requestMapper.toRequest(Mockito.<Long>any(), Mockito.any()))
                .thenThrow(new NotFoundException("An error occurred"));

        RequestCreateDto requestCreateDto = new RequestCreateDto();
        requestCreateDto.setDescription("The characteristics of someone or something");

        assertThrows(NotFoundException.class, () -> requestServiceImpl.create(1L, requestCreateDto));
        verify(requestMapper).toRequest(Mockito.<Long>any(), Mockito.any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
    }

    @Test
    void testGetAllUserById() {
        when(requestRepository.findAllByAuthor_id(Mockito.<Long>any())).thenReturn(new ArrayList<>());

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        when(itemRepository.findAllByRequest_idIn(Mockito.any())).thenReturn(new ArrayList<>());
        when(itemMapper.toItemDtoList(Mockito.any())).thenReturn(new ArrayList<>());

        List<RequestDto> actualAllUserById = requestServiceImpl.getAllUserById(1L);

        verify(itemMapper).toItemDtoList(Mockito.any());
        verify(itemRepository).findAllByRequest_idIn(Mockito.any());
        verify(requestRepository).findAllByAuthor_id(Mockito.<Long>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
        assertTrue(actualAllUserById.isEmpty());
    }

    @Test
    void testGetAllUserById2() {
        when(requestRepository.findAllByAuthor_id(Mockito.<Long>any())).thenReturn(new ArrayList<>());

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        when(itemRepository.findAllByRequest_idIn(Mockito.any())).thenReturn(new ArrayList<>());
        when(itemMapper.toItemDtoList(Mockito.any())).thenThrow(new NotFoundException("An error occurred"));

        assertThrows(NotFoundException.class, () -> requestServiceImpl.getAllUserById(1L));
        verify(itemMapper).toItemDtoList(Mockito.any());
        verify(itemRepository).findAllByRequest_idIn(Mockito.any());
        verify(requestRepository).findAllByAuthor_id(Mockito.<Long>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
    }

    @Test
    void testGetAllUserById3() {
        User author = new User();
        author.setEmail("jane.doe@example.org");
        author.setId(1L);
        author.setName("Name");

        Request request = new Request();
        request.setAuthor(author);
        request.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);

        ArrayList<Request> requestList = new ArrayList<>();
        requestList.add(request);
        when(requestRepository.findAllByAuthor_id(Mockito.<Long>any())).thenReturn(requestList);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        RequestDto.RequestDtoBuilder builderResult = RequestDto.builder();
        RequestDto.RequestDtoBuilder idResult = builderResult.created(LocalDate.of(1970, 1, 1).atStartOfDay())
                .description("The characteristics of someone or something")
                .id(1L);
        ArrayList<ItemDto> items = new ArrayList<>();
        RequestDto buildResult = idResult.items(items).build();
        when(requestMapper.toRequestDto(Mockito.any())).thenReturn(buildResult);
        when(itemRepository.findAllByRequest_idIn(Mockito.any())).thenReturn(new ArrayList<>());
        when(itemMapper.toItemDtoList(Mockito.any())).thenReturn(new ArrayList<>());

        List<RequestDto> actualAllUserById = requestServiceImpl.getAllUserById(1L);

        verify(itemMapper).toItemDtoList(Mockito.any());
        verify(itemRepository).findAllByRequest_idIn(Mockito.any());
        verify(requestMapper).toRequestDto(Mockito.any());
        verify(requestRepository).findAllByAuthor_id(Mockito.<Long>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
        assertEquals(1, actualAllUserById.size());
        assertEquals(items, actualAllUserById.get(0).getItems());
    }

    @Test
    void testGetAllUserById4() {
        User author = new User();
        author.setEmail("jane.doe@example.org");
        author.setId(1L);
        author.setName("Name");

        Request request = new Request();
        request.setAuthor(author);
        request.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);

        User author2 = new User();
        author2.setEmail("john.smith@example.org");
        author2.setId(2L);
        author2.setName("42");

        Request request2 = new Request();
        request2.setAuthor(author2);
        request2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request2.setDescription("Description");
        request2.setId(2L);

        ArrayList<Request> requestList = new ArrayList<>();
        requestList.add(request2);
        requestList.add(request);
        when(requestRepository.findAllByAuthor_id(Mockito.<Long>any())).thenReturn(requestList);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        RequestDto.RequestDtoBuilder builderResult = RequestDto.builder();
        RequestDto.RequestDtoBuilder idResult = builderResult.created(LocalDate.of(1970, 1, 1).atStartOfDay())
                .description("The characteristics of someone or something")
                .id(1L);
        ArrayList<ItemDto> items = new ArrayList<>();
        RequestDto buildResult = idResult.items(items).build();
        when(requestMapper.toRequestDto(Mockito.any())).thenReturn(buildResult);
        when(itemRepository.findAllByRequest_idIn(Mockito.any())).thenReturn(new ArrayList<>());
        when(itemMapper.toItemDtoList(Mockito.any())).thenReturn(new ArrayList<>());

        List<RequestDto> actualAllUserById = requestServiceImpl.getAllUserById(1L);

        verify(itemMapper).toItemDtoList(Mockito.any());
        verify(itemRepository).findAllByRequest_idIn(Mockito.any());
        verify(requestMapper, atLeast(1)).toRequestDto(Mockito.any());
        verify(requestRepository).findAllByAuthor_id(Mockito.<Long>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
        assertEquals(2, actualAllUserById.size());
        assertEquals(items, actualAllUserById.get(0).getItems());
    }

    @Test
    void testGetAll() {
        when(requestRepository.findAllByAuthor_idNot(Mockito.<Long>any(), Mockito.any()))
                .thenReturn(new ArrayList<>());

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        when(itemRepository.findAllByRequest_idIn(Mockito.any())).thenReturn(new ArrayList<>());
        when(itemMapper.toItemDtoList(Mockito.any())).thenReturn(new ArrayList<>());

        List<RequestDto> actualAll = requestServiceImpl.getAll(1L, null);

        verify(itemMapper).toItemDtoList(Mockito.any());
        verify(itemRepository).findAllByRequest_idIn(Mockito.any());
        verify(requestRepository).findAllByAuthor_idNot(Mockito.<Long>any(), Mockito.any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
        assertTrue(actualAll.isEmpty());
    }

    @Test
    void testGetAll2() {
        when(requestRepository.findAllByAuthor_idNot(Mockito.<Long>any(), Mockito.any()))
                .thenReturn(new ArrayList<>());

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        when(itemRepository.findAllByRequest_idIn(Mockito.any())).thenReturn(new ArrayList<>());
        when(itemMapper.toItemDtoList(Mockito.any())).thenThrow(new NotFoundException("An error occurred"));

        assertThrows(NotFoundException.class, () -> requestServiceImpl.getAll(1L, null));
        verify(itemMapper).toItemDtoList(Mockito.any());
        verify(itemRepository).findAllByRequest_idIn(Mockito.any());
        verify(requestRepository).findAllByAuthor_idNot(Mockito.<Long>any(), Mockito.any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
    }

    @Test
    void testGetAll3() {
        User author = new User();
        author.setEmail("jane.doe@example.org");
        author.setId(1L);
        author.setName("Name");

        Request request = new Request();
        request.setAuthor(author);
        request.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);

        ArrayList<Request> requestList = new ArrayList<>();
        requestList.add(request);
        when(requestRepository.findAllByAuthor_idNot(Mockito.<Long>any(), Mockito.any())).thenReturn(requestList);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        RequestDto.RequestDtoBuilder builderResult = RequestDto.builder();
        RequestDto.RequestDtoBuilder idResult = builderResult.created(LocalDate.of(1970, 1, 1).atStartOfDay())
                .description("The characteristics of someone or something")
                .id(1L);
        ArrayList<ItemDto> items = new ArrayList<>();
        RequestDto buildResult = idResult.items(items).build();
        when(requestMapper.toRequestDto(Mockito.any())).thenReturn(buildResult);
        when(itemRepository.findAllByRequest_idIn(Mockito.any())).thenReturn(new ArrayList<>());
        when(itemMapper.toItemDtoList(Mockito.any())).thenReturn(new ArrayList<>());

        List<RequestDto> actualAll = requestServiceImpl.getAll(1L, null);

        verify(itemMapper).toItemDtoList(Mockito.any());
        verify(itemRepository).findAllByRequest_idIn(Mockito.any());
        verify(requestMapper).toRequestDto(Mockito.any());
        verify(requestRepository).findAllByAuthor_idNot(Mockito.<Long>any(), Mockito.any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
        assertEquals(1, actualAll.size());
        assertEquals(items, actualAll.get(0).getItems());
    }

    @Test
    void testGetAll4() {
        User author = new User();
        author.setEmail("jane.doe@example.org");
        author.setId(1L);
        author.setName("Name");

        Request request = new Request();
        request.setAuthor(author);
        request.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);

        User author2 = new User();
        author2.setEmail("john.smith@example.org");
        author2.setId(2L);
        author2.setName("42");

        Request request2 = new Request();
        request2.setAuthor(author2);
        request2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request2.setDescription("Description");
        request2.setId(2L);

        ArrayList<Request> requestList = new ArrayList<>();
        requestList.add(request2);
        requestList.add(request);
        when(requestRepository.findAllByAuthor_idNot(Mockito.<Long>any(), Mockito.any())).thenReturn(requestList);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        RequestDto.RequestDtoBuilder builderResult = RequestDto.builder();
        RequestDto.RequestDtoBuilder idResult = builderResult.created(LocalDate.of(1970, 1, 1).atStartOfDay())
                .description("The characteristics of someone or something")
                .id(1L);
        ArrayList<ItemDto> items = new ArrayList<>();
        RequestDto buildResult = idResult.items(items).build();
        when(requestMapper.toRequestDto(Mockito.any())).thenReturn(buildResult);
        when(itemRepository.findAllByRequest_idIn(Mockito.any())).thenReturn(new ArrayList<>());
        when(itemMapper.toItemDtoList(Mockito.any())).thenReturn(new ArrayList<>());

        List<RequestDto> actualAll = requestServiceImpl.getAll(1L, null);

        verify(itemMapper).toItemDtoList(Mockito.any());
        verify(itemRepository).findAllByRequest_idIn(Mockito.any());
        verify(requestMapper, atLeast(1)).toRequestDto(Mockito.any());
        verify(requestRepository).findAllByAuthor_idNot(Mockito.<Long>any(), Mockito.any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
        assertEquals(2, actualAll.size());
        assertEquals(items, actualAll.get(0).getItems());
    }

    @Test
    void testValidateFindRequestById() {
        User author = new User();
        author.setEmail("jane.doe@example.org");
        author.setId(1L);
        author.setName("Name");

        Request request = new Request();
        request.setAuthor(author);
        request.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        Optional<Request> ofResult = Optional.of(request);
        when(requestRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Request actualValidateFindRequestByIdResult = requestServiceImpl.validateFindRequestById(1L);

        verify(requestRepository).findById(Mockito.<Long>any());
        assertSame(request, actualValidateFindRequestByIdResult);
    }

    @Test
    void testValidateFindRequestById2() {
        Optional<Request> emptyResult = Optional.empty();
        when(requestRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        assertThrows(NotFoundException.class, () -> requestServiceImpl.validateFindRequestById(1L));
        verify(requestRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testValidateFindRequestById3() {
        when(requestRepository.findById(Mockito.<Long>any())).thenThrow(new NotFoundException("An error occurred"));

        assertThrows(NotFoundException.class, () -> requestServiceImpl.validateFindRequestById(1L));
        verify(requestRepository).findById(Mockito.<Long>any());
    }


    @Test
    void testGetById() {
        User author = new User();
        author.setEmail("jane.doe@example.org");
        author.setId(1L);
        author.setName("Name");

        Request request = new Request();
        request.setAuthor(author);
        request.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        Optional<Request> ofResult = Optional.of(request);
        when(requestRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        RequestDto.RequestDtoBuilder builderResult = RequestDto.builder();
        RequestDto.RequestDtoBuilder idResult = builderResult.created(LocalDate.of(1970, 1, 1).atStartOfDay())
                .description("The characteristics of someone or something")
                .id(1L);
        ArrayList<ItemDto> items = new ArrayList<>();
        RequestDto buildResult = idResult.items(items).build();
        when(requestMapper.toRequestDto(Mockito.any())).thenReturn(buildResult);
        when(itemRepository.findAllByRequest_id(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(itemMapper.toItemDtoList(Mockito.any())).thenReturn(new ArrayList<>());

        RequestDto actualById = requestServiceImpl.getById(1L, 1L);

        verify(requestRepository).findById(Mockito.<Long>any());
        verify(itemMapper).toItemDtoList(Mockito.any());
        verify(itemRepository).findAllByRequest_id(Mockito.<Long>any());
        verify(requestMapper).toRequestDto(Mockito.any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
        assertEquals(items, actualById.getItems());
    }

    @Test
    void testGetById2() {
        User author = new User();
        author.setEmail("jane.doe@example.org");
        author.setId(1L);
        author.setName("Name");

        Request request = new Request();
        request.setAuthor(author);
        request.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        Optional<Request> ofResult = Optional.of(request);
        when(requestRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);
        RequestDto.RequestDtoBuilder builderResult = RequestDto.builder();
        RequestDto.RequestDtoBuilder idResult = builderResult.created(LocalDate.of(1970, 1, 1).atStartOfDay())
                .description("The characteristics of someone or something")
                .id(1L);
        RequestDto buildResult = idResult.items(new ArrayList<>()).build();
        when(requestMapper.toRequestDto(Mockito.any())).thenReturn(buildResult);
        when(itemRepository.findAllByRequest_id(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(itemMapper.toItemDtoList(Mockito.any())).thenThrow(new NotFoundException("An error occurred"));

        assertThrows(NotFoundException.class, () -> requestServiceImpl.getById(1L, 1L));
        verify(requestRepository).findById(Mockito.<Long>any());
        verify(itemMapper).toItemDtoList(Mockito.any());
        verify(itemRepository).findAllByRequest_id(Mockito.<Long>any());
        verify(requestMapper).toRequestDto(Mockito.any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
    }

    @Test
    void testGetById3() {
        Optional<Request> emptyResult = Optional.empty();
        when(requestRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userService.validationFindUserById(Mockito.<Long>any())).thenReturn(user);

        assertThrows(NotFoundException.class, () -> requestServiceImpl.getById(1L, 1L));
        verify(requestRepository).findById(Mockito.<Long>any());
        verify(userService).validationFindUserById(Mockito.<Long>any());
    }
}
