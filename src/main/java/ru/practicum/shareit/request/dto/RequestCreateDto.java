package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RequestCreateDto {
    @NotBlank(message = "Описание пустое")
    private String description;
}
