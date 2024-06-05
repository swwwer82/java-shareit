package ru.practicum.shareit.request.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class RequestCreateDto {
    @NotBlank(message = "Описание пустое")
    private String description;
}
