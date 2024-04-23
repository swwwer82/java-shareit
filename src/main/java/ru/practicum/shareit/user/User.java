package ru.practicum.shareit.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String name;
    private String email;
}
