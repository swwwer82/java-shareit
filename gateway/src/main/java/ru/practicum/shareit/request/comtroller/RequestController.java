package ru.practicum.shareit.request.comtroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.RequestClient;
import ru.practicum.shareit.request.dto.RequestCreateDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {

    private final RequestClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                         @Valid @RequestBody RequestCreateDto requestCreateDto) {
        log.info("User {} create item request {}", userId, requestCreateDto);
        return client.create(userId, requestCreateDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUser(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        log.info("User {} get all item request", userId);
        return client.getAllUserById(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                         @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int fromIndex,
                                         @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        log.info("User {} get all item request", userId);
        return client.getAll(userId, fromIndex, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                          @PathVariable @Positive long requestId) {
        log.info("User {} get item request {}", userId, requestId);
        return client.getById(userId, requestId);
    }

}
