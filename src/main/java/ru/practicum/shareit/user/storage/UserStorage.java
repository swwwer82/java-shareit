package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserStorage {
    User insert(User user);

    User update(User user);

    void delete(Long id);

    User findById(Long id);

    List<User> findAll();
}
