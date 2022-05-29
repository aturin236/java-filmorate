package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.practicum.filmorate.controller.dto.UserDTO;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.InMemoryUserService;
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
                new InMemoryUserService(
                        UserStorageFactory.getDefault()
                )
        );
    }

    @Test
    void should_ThrowException_WhenLoginWithWhitespace() {
        UserDTO user = UserDTO.builder()
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
    void should_ThrowException_WhenEmailFailed(UserDTO user) {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.addUser(user));
        assertTrue(exception.getMessage().contains("Почта не соответствует формату email"));
    }

    static Stream<UserDTO> argsProviderFactoryForTestEmail() {
        UserDTO user1 = UserDTO.builder()
                .login("логин1")
                .name("логин")
                .id(1)
                .email("mailmail.ru")
                .birthday(LocalDate.of(1988, 12, 12))
                .build();
        UserDTO user2 = UserDTO.builder()
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
        UserDTO user = UserDTO.builder()
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
        UserDTO user = UserDTO.builder()
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