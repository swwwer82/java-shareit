package ru.practicum.shareit.booking.util;

import ru.practicum.shareit.exceptions.exception.NoSuchEnumException;

public enum BookingState {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static BookingState fromStringIgnoreCase(String data) {
        if (data != null) {
            for (BookingState sortType : BookingState.values()) {
                if (data.equalsIgnoreCase(sortType.toString())) {
                    return sortType;
                }
            }
        }
        throw new NoSuchEnumException(String.format("Unknown state: %s", data));
    }
}
