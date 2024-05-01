package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item toItem(ItemCreateDto itemCreateDto);

    Item toItem(ItemUpdateDto itemUpdateDto);

    ItemDto toItemDto(Item item);

    List<ItemDto> toItemDtoList(List<Item> items);
}
