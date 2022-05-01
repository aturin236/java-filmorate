package ru.yandex.practicum.filmorate.model.service;

public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
