package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private Request request;
}
