package ru.practicum.shareit.item.dto;

import lombok.*;
import org.springframework.stereotype.Component;

/**
 * TODO Sprint add-controllers.
 */
@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
}
