package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserInMemoryStorage implements UserStorage {
    private final Map<Long, User> idToUsers = new HashMap<>();
    private Long id = 1L;

    private Long getNextId() {
        return ++id;
    }


    @Override
    public User insert(User user) {
        user.setId(getNextId());
        idToUsers.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        return idToUsers.put(user.getId(), user);
    }

    @Override
    public void delete(Long id) {
        idToUsers.remove(id);
    }

    @Override
    public User findById(Long id) {
        return idToUsers.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(idToUsers.values());
    }
}
