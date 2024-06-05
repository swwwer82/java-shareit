package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.pagination.PaginationCustom;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(value = "from", defaultValue = "0") int fromIndex,
                                                @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Get all item by user id {}", userId);
        List<ItemDto> items = itemService.getAll(userId, PaginationCustom.getPageableFromIndex(fromIndex, size));
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> get(@PathVariable("id") Long itemId,
                                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get item by id {}", itemId);
        ItemDto item = itemService.get(itemId, userId);
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<ItemDto> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @Valid @RequestBody ItemCreateDto itemCreateDto) {
        log.info("Create item {}, userid {}", itemCreateDto, userId);
        ItemDto item = itemService.create(userId, itemCreateDto);
        return ResponseEntity.ok(item);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable("id") Long itemId,
                                          @Valid @RequestBody ItemUpdateDto itemUpdateDto) {
        log.info("Update item {}, userId {}", itemUpdateDto, userId);
        ItemDto item = itemService.update(userId, itemId, itemUpdateDto);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> search(@RequestParam("text") String text,
                                                @RequestParam(value = "from", defaultValue = "0") int fromIndex,
                                                @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Search items - {}", text);
        List<ItemDto> items = itemService.search(text, PaginationCustom.getPageableFromIndex(fromIndex, size));
        return ResponseEntity.ok(items);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @PathVariable Long itemId,
                                                    @Valid @RequestBody CommentCreateDto commentCreateDto) {
        log.info("User {} for item {} create comment {}", userId, itemId, commentCreateDto);
        CommentDto comment = itemService.createComment(userId, itemId, commentCreateDto);
        return ResponseEntity.ok(comment);
    }
}
