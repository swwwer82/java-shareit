package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * TODO Sprint add-controllers.
 */
@Component
@Getter
@Setter
public class User {
    private Integer id;
    private String name;
    private String email;
}
