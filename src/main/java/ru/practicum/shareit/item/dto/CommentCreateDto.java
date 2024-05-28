package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CommentCreateDto {
    @NotBlank(message = "Пустой комментарий")
    private String text;
}