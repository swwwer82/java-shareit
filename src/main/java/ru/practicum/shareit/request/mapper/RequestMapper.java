package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface RequestMapper {
    @Mapping(source = "userId", target = "author.id")
    @Mapping(target = "created", expression = "java(LocalDateTime.now())")
    Request toRequest(Long userId, RequestCreateDto requestCreateDto);

    RequestDto toRequestDto(Request request);
}
