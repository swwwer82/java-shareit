package ru.practicum.shareit.exception.exeption;

public class NotValidRequestException extends RuntimeException {

    public NotValidRequestException(String message) {
        super(message);
    }
}
