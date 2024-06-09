package ru.practicum.shareit.item.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CommentCreateDto {
    @NotBlank(message = "Пустой комментарий")
    private String text;
}