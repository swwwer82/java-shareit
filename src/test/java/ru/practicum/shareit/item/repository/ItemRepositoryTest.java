package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User createUserDefault() {
        return userRepository.save(User.builder()
                .name(UUID.randomUUID().toString())
                .email(String.format("%s@%s", UUID.randomUUID(), UUID.randomUUID()))
                .build());
    }

    private Item createDefaultItem() {
        return itemRepository.save(Item.builder()
                .name("name")
                .owner(User.builder().id(createUserDefault().getId()).build())
                .description("description")
                .available(true)
                .build());
    }

    @Test
    void search() {
        Item item = createDefaultItem();

        List<Item> result = itemRepository.search("crip", PageRequest.of(0, 10));

        assertEquals(1, result.size());
        assertEquals(item.getId(), result.get(0).getId());
    }
}