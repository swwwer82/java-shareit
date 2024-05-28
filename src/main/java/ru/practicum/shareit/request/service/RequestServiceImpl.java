package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;
    private final RequestMapper requestMapper;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public RequestDto create(Long userId, RequestCreateDto requestCreateDto) {
        userService.validationFindUserById(userId);
        return requestMapper.toRequestDto(requestRepository.save(requestMapper.toRequest(userId, requestCreateDto)));
    }

    @Override
    public List<RequestDto> getAllUserById(Long userId) {
        userService.validationFindUserById(userId);
        return addItemsToDto(requestRepository.findAllByAuthor_id(userId));
    }

    @Override
    public List<RequestDto> getAll(Long userId, Pageable pageable) {
        userService.validationFindUserById(userId);
        return addItemsToDto(requestRepository.findAllByAuthor_idNot(userId, pageable));
    }

    private List<RequestDto> addItemsToDto(List<Request> requestList) {
        List<Long> requestIdList = requestList.stream().map(Request::getId).collect(Collectors.toList());

        Map<Long, List<ItemDto>> itemMap = itemMapper.toItemDtoList(itemRepository.findAllByRequest_idIn(requestIdList))
                .stream()
                .collect(Collectors.groupingBy(ItemDto::getRequestId));

        return requestList.stream()
                .map(requestMapper::toRequestDto)
                .peek(requestDto -> requestDto.setItems(itemMap.getOrDefault(requestDto.getId(), new ArrayList<>())))
                .collect(Collectors.toList());
    }

    @Override
    public Request validateFindRequestById(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Запрос на объект не найден"));
    }

    @Override
    public RequestDto getById(Long userId, Long requestId) {
        userService.validationFindUserById(userId);
        RequestDto requestDto = requestMapper.toRequestDto(validateFindRequestById(requestId));
        List<ItemDto> items = itemMapper.toItemDtoList(itemRepository.findAllByRequest_id(requestId));
        requestDto.setItems(items);
        return requestDto;
    }
}
