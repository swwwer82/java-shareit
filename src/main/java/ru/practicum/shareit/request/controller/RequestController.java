package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.pagination.PaginationCustom;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<RequestDto> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Valid @RequestBody RequestCreateDto requestCreateDto) {
        log.info("Пользователь {} создал запрос на вещь {}", userId, requestCreateDto);
        RequestDto requestDto = requestService.create(userId, requestCreateDto);
        return ResponseEntity.ok(requestDto);
    }

    @GetMapping
    public ResponseEntity<List<RequestDto>> getAllUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Пользователь {} получает все запросы на вещи", userId);
        List<RequestDto> requestDtos = requestService.getAllUserById(userId);
        return ResponseEntity.ok(requestDtos);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RequestDto>> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(value = "from", defaultValue = "0") int fromIndex,
                                                   @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Пользователь {} получает все запросы на все вещи", userId);
        List<RequestDto> requestDtos = requestService.getAll(userId, PaginationCustom.getPageableFromIndex(fromIndex, size));
        return ResponseEntity.ok(requestDtos);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<RequestDto> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PathVariable Long requestId) {
        log.info("Пользователь {} получает запрос на вещь {}", userId, requestId);
        RequestDto requestDto = requestService.getById(userId, requestId);
        return ResponseEntity.ok(requestDto);
    }
}