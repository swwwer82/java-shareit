package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto get(Long itemId, Long userId);

    List<ItemDto> getAll(Long userId, Pageable pageable);

    ItemDto create(Long userId, ItemCreateDto itemCreateDto);

    ItemDto update(Long userId, Long itemId, ItemUpdateDto itemUpdateDto);

    List<ItemDto> search(String text, Pageable pageable);

    Item validationFindItemById(Long itemId);

    Item validationOwnerUserById(Long userId, Long itemId);

    CommentDto createComment(Long userId, Long itemId, CommentCreateDto commentCreateDto);
}
