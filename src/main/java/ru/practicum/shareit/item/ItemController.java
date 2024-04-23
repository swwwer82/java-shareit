package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> create(
            @RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid ItemDto itemDto) {
        return ResponseEntity.ok().body(itemService.create(userId, itemDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> updateFields(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long id,
            @RequestBody Map<String, Object> fields) {
        return ResponseEntity.ok().body(itemService.updateFields(userId, id, fields));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> findItemById(@PathVariable Long id) {
        return ResponseEntity.ok().body(itemService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> findAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok().body(itemService.findAllByUserId(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> search(@RequestParam String text) {
        return ResponseEntity.ok().body(itemService.search(text));
    }
}