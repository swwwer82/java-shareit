package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface CommentMapper {

    @Mapping(source = "userId", target = "author.id")
    @Mapping(source = "itemId", target = "item.id")
    @Mapping(target = "created", expression = "java(LocalDateTime.now())")
    Comment toComment(Long userId, Long itemId, CommentCreateDto commentCreateDto);

    @Mapping(source = "author.name", target = "authorName")
    CommentDto toCommentDto(Comment comment);

    List<CommentDto> toCommentDto(List<Comment> comments);
}