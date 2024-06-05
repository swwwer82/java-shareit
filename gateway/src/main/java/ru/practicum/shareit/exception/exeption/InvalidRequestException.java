package ru.practicum.shareit.exception.exeption;


public class InvalidRequestException extends RuntimeException{
    public InvalidRequestException(String message) {
        super(message);
    }
}