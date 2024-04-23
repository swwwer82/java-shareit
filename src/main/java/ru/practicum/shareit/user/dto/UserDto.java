package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class UserDto {
    private Integer id;
    private String name;
    private String email;
}
