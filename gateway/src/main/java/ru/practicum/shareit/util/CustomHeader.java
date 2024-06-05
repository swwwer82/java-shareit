package ru.practicum.shareit.util;

import lombok.Getter;

@Getter
public enum CustomHeader {

    USERID("X-Sharer-User-Id");

    private final String name;

    CustomHeader(String name) {
        this.name = name;
    }
}
