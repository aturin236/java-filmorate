package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.factory.UserStorageFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;

    @BeforeEach
    void createContext() {
        userController = new UserController(
                UserStorageFactory.getDefault(),
                new UserService(
                        UserStorageFactory.getDefault()
                )
        );
    }

    @Test
    void should_ThrowException_WhenLoginWithWhitespace() {
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

    @ParameterizedTest
    @MethodSource("argsProviderFactoryForTestEmail")
    void should_ThrowException_WhenEmailFailed(User user) {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.addUser(user));
        assertTrue(exception.getMessage().contains("Почта не соответствует формату email"));
    }

    static Stream<User> argsProviderFactoryForTestEmail() {
        User user1 = User.builder()
                .login("логин1")
                .name("логин")
                .id(1)
                .email("mailmail.ru")
                .birthday(LocalDate.of(1988, 12, 12))
                .build();
        User user2 = User.builder()
                .login("логин1")
                .name("логин")
                .id(1)
                .email("")
                .birthday(LocalDate.of(1988, 12, 12))
                .build();
        return Stream.of(user1, user2);
    }

    @Test
    void should_ThrowException_WhenBirthdayFromFuture() {
        User user = User.builder()
                .login("логин1")
                .name("логин")
                .id(1)
                .email("mail@mail.ru")
                .birthday(LocalDateTime.now().toLocalDate().plusDays(10))
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.addUser(user));
        assertTrue(exception.getMessage().contains("Дата рождения находится в будущем"));
    }

    @Test
    void should_AddUser_WithSuccess() throws ValidationException {
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