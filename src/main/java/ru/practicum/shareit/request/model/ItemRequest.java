package ru.practicum.shareit.request.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Component
@Getter
@Setter
public class ItemRequest {
    private Integer id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
