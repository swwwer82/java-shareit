package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class UserUpdateDto {
    @Email(message = "Некорректный формат емэйла")
    private String email;
    private String name;
}
