package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.groups.Group;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
@Builder
public class UserDto {
    @Null(groups = Group.OnInsert.class)
    @NotNull(groups = Group.OnUpdate.class)
    private Long id;

    @NotBlank(groups = Group.OnInsert.class)
    private String name;

    @Email
    private String email;
}