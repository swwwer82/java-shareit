package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class UserCreateDto {
    @Email(message = "Некорректный формат емэйла")
    @NotBlank(message = "Email обязательный")
    private String email;
    @NotBlank(message = "Name обязательный")
    private String name;
}
