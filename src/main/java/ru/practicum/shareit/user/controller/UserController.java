package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        log.info("Получить всех пользователей");
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> get(@PathVariable("id") long userId) {
        log.info("Получить пользователя по id {}", userId);
        return ResponseEntity.ok(userService.get(userId));
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserCreateDto user) {
        log.info("Создать пользователя {}", user);
        return ResponseEntity.ok(userService.create(user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable("id") long userId,
                                          @Valid @RequestBody UserUpdateDto user) {
        log.info("Обновить пользователя {}, данные {}", user, user);
        return ResponseEntity.ok(userService.update(userId, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long userId) {
        log.info("Удалить пользователя id {}", userId);
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}