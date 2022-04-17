package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.service.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;

    @BeforeEach
    void createContext() {
        userController = new UserController();
    }

    @Test
    void Should_ThrowException_WhenLoginWithWhitespace() {
        User user = User.builder()
                .login("логин 1")
                .name("логин")
                .id(1)
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1988, 12, 12))
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.addUser(user));
        assertTrue(exception.getMessage().contains("Логин содержит пробелы"));
    }

    @Test
    void Should_AddUser_WithSuccess() throws ValidationException {
        User user = User.builder()
                .login("логин1")
                .name("логин")
                .id(1)
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1988, 12, 12))
                .build();
        userController.addUser(user);
        assertEquals(1, userController.getAllUsers().size());
    }
}