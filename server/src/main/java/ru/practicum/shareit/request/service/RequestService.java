package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestService {

    RequestDto create(Long userId, RequestCreateDto requestCreateDto);

    List<RequestDto> getAllUserById(Long userId);

    Request validateFindRequestById(Long requestId);

    RequestDto getById(Long userId, Long requestId);

    List<RequestDto> getAll(Long userId, Pageable pageable);
}
