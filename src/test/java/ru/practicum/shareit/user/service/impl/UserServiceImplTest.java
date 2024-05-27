package ru.practicum.shareit.user.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.exception.exeption.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Test
    void testGetAll() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        List<UserDto> actualAll = userServiceImpl.getAll();

        verify(userRepository).findAll();
        assertTrue(actualAll.isEmpty());
    }

    @Test
    void testGet() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        UserDto buildResult = UserDto.builder().email("jane.doe@example.org").id(1L).name("Name").build();
        when(userMapper.toUserDto(Mockito.any())).thenReturn(buildResult);

        userServiceImpl.get(1L);

        verify(userRepository).findById(Mockito.<Long>any());
        verify(userMapper).toUserDto(Mockito.any());
    }

    @Test
    void testGet2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(userMapper.toUserDto(Mockito.any())).thenThrow(new NotFoundException("An error occurred"));

        assertThrows(NotFoundException.class, () -> userServiceImpl.get(1L));
        verify(userRepository).findById(Mockito.<Long>any());
        verify(userMapper).toUserDto(Mockito.any());
    }

    @Test
    void testGet3() {
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        assertThrows(NotFoundException.class, () -> userServiceImpl.get(1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testCreate() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userRepository.save(Mockito.any())).thenReturn(user);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");
        UserDto buildResult = UserDto.builder().email("jane.doe@example.org").id(1L).name("Name").build();
        when(userMapper.toUserDto(Mockito.any())).thenReturn(buildResult);
        when(userMapper.toUser(Mockito.any())).thenReturn(user2);

        userServiceImpl.create(null);

        verify(userRepository).save(Mockito.any());
        verify(userMapper).toUser(Mockito.any());
        verify(userMapper).toUserDto(Mockito.any());
    }

    @Test
    void testCreate2() {
        when(userMapper.toUser(Mockito.any())).thenThrow(new NotFoundException("An error occurred"));

        assertThrows(NotFoundException.class, () -> userServiceImpl.create(null));
        verify(userMapper).toUser(Mockito.any());
    }

    @Test
    void testCreate3() {
        when(userRepository.save(Mockito.any())).thenThrow(new NotFoundException("An error occurred"));

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userMapper.toUser(Mockito.any())).thenReturn(user);
        UserCreateDto data = UserCreateDto.builder().email("jane.doe@example.org").name("Name").build();

        assertThrows(NotFoundException.class, () -> userServiceImpl.create(data));
        verify(userRepository).save(Mockito.any());
        verify(userMapper).toUser(Mockito.any());
    }

    @Test
    void testUpdate() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");
        UserDto buildResult = UserDto.builder().email("jane.doe@example.org").id(1L).name("Name").build();
        when(userMapper.toUserDto(Mockito.any())).thenReturn(buildResult);
        when(userMapper.toUser(Mockito.<Long>any(), Mockito.any())).thenReturn(user2);

        userServiceImpl.update(1L, new UserUpdateDto());

        verify(userRepository).findById(Mockito.<Long>any());
        verify(userMapper).toUser(Mockito.<Long>any(), Mockito.any());
        verify(userMapper).toUserDto(Mockito.any());
    }

    @Test
    void testUpdate2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");
        when(userMapper.toUserDto(Mockito.any())).thenThrow(new NotFoundException("An error occurred"));
        when(userMapper.toUser(Mockito.<Long>any(), Mockito.any())).thenReturn(user2);

        assertThrows(NotFoundException.class, () -> userServiceImpl.update(1L, new UserUpdateDto()));
        verify(userRepository).findById(Mockito.<Long>any());
        verify(userMapper).toUser(Mockito.<Long>any(), Mockito.any());
        verify(userMapper).toUserDto(Mockito.any());
    }

    @Test
    void testUpdate3() {
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userMapper.toUser(Mockito.<Long>any(), Mockito.any())).thenReturn(user);

        assertThrows(NotFoundException.class, () -> userServiceImpl.update(1L, new UserUpdateDto()));
        verify(userRepository).findById(Mockito.<Long>any());
        verify(userMapper).toUser(Mockito.<Long>any(), Mockito.any());
    }

    @Test
    void testDelete() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        Optional<User> ofResult = Optional.of(user);
        doNothing().when(userRepository).deleteById(Mockito.<Long>any());
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        userServiceImpl.delete(1L);

        verify(userRepository).deleteById(Mockito.<Long>any());
        verify(userRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testDelete2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        Optional<User> ofResult = Optional.of(user);
        doThrow(new NotFoundException("An error occurred")).when(userRepository).deleteById(Mockito.<Long>any());
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        assertThrows(NotFoundException.class, () -> userServiceImpl.delete(1L));
        verify(userRepository).deleteById(Mockito.<Long>any());
        verify(userRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testDelete3() {
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        assertThrows(NotFoundException.class, () -> userServiceImpl.delete(1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testValidationFindUserById() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        User actualValidationFindUserByIdResult = userServiceImpl.validationFindUserById(1L);

        verify(userRepository).findById(Mockito.<Long>any());
        assertSame(user, actualValidationFindUserByIdResult);
    }

    @Test
    void testValidationFindUserById2() {
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        assertThrows(NotFoundException.class, () -> userServiceImpl.validationFindUserById(1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testValidationFindUserById3() {
        when(userRepository.findById(Mockito.<Long>any())).thenThrow(new NotFoundException("An error occurred"));

        assertThrows(NotFoundException.class, () -> userServiceImpl.validationFindUserById(1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }
}
