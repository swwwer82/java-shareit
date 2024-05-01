package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получить все вещи для пользователя id {}", userId);
        return ResponseEntity.ok(itemService.getAll(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> get(@PathVariable("id") Long itemId,
                                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получить вещь по id {}", itemId);
        return ResponseEntity.ok(itemService.get(itemId, userId));
    }

    @PostMapping
    public ResponseEntity<ItemDto> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @Valid @RequestBody ItemCreateDto itemCreateDto) {
        log.info("создать вещь {}, userid {}", itemCreateDto, userId);
        return ResponseEntity.ok(itemService.create(userId, itemCreateDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable("id") Long itemId,
                                          @Valid @RequestBody ItemUpdateDto itemUpdateDto) {
        log.info("обновить вещи {}, пользователя {}", itemUpdateDto, userId);
        return ResponseEntity.ok(itemService.update(userId, itemId, itemUpdateDto));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> search(@RequestParam("text") String text) {
        log.info("Найти вещь - {}", text);
        return ResponseEntity.ok(itemService.search(text));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @PathVariable Long itemId,
                                                    @Valid @RequestBody CommentCreateDto commentCreateDto) {
        log.info("Пользователь {} для вещи {} написал комментарий {}", userId, itemId, commentCreateDto);
        return ResponseEntity.ok(itemService.createComment(userId, itemId, commentCreateDto));
    }
}
