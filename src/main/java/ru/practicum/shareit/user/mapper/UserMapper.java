package ru.practicum.shareit.user.mapper;

import lombok.Getter;
import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreateDto userCreateDto);

    User toUser(Long userId, UserUpdateDto userUpdateDto);

    UserDto toUserDto(User user);

}
