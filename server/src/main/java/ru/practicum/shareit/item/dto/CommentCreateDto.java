package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentCreateDto {
    @NotBlank(message = "Пустой комментарий")
    private String text;
}