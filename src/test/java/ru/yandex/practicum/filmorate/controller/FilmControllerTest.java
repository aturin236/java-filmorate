package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.service.ValidationException;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    void createContext() {
        filmController = new FilmController();
    }

    @Test
    void should_ThrowException_WhenReleaseDateOld() {
        Film film = Film.builder()
                .name("Фильм")
                .description("")
                .id(1)
                .duration((short) 100)
                .releaseDate(LocalDate.of(1789, 12, 12))
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film));
        assertTrue(exception.getMessage().contains("Дата фильма меньше"));
    }

    @ParameterizedTest
    @MethodSource("argsProviderFactoryForTestLogin")
    void should_ThrowException_WhenLoginNullOrEmpty(Film film) {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film));
        assertTrue(exception.getMessage().contains("Наименование должно быть заполнено"));
    }

    static Stream<Film> argsProviderFactoryForTestLogin() {
        Film film1 = Film.builder()
                .name(null)
                .description("")
                .id(1)
                .duration((short) 100)
                .releaseDate(LocalDate.of(1989, 12, 12))
                .build();
        Film film2 = Film.builder()
                .name("")
                .description("")
                .id(1)
                .duration((short) 100)
                .releaseDate(LocalDate.of(1789, 12, 12))
                .build();
        return Stream.of(film1, film2);
    }

    @Test
    void should_ThrowException_WhenDescriptionTooLong() {
        String sb = "1".repeat(201);
        Film film = Film.builder()
                .name("Фильм")
                .description(sb)
                .id(1)
                .duration((short) 100)
                .releaseDate(LocalDate.of(1989, 12, 12))
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film));
        assertTrue(exception.getMessage().contains("Длина описания не должна превышать"));
    }

    @Test
    void should_ThrowException_WhenDurationLess0() {
        Film film = Film.builder()
                .name("Фильм")
                .description("")
                .id(1)
                .duration((short) -100)
                .releaseDate(LocalDate.of(1989, 12, 12))
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film));
        assertTrue(exception.getMessage().contains("Продолжительность фильма должна быть положительной"));
    }

    @Test
    void should_AddFilm_WithSuccess() throws ValidationException {
        Film film = Film.builder()
                .name("Фильм")
                .description("")
                .id(1)
                .duration((short) 100)
                .releaseDate(LocalDate.of(1989, 12, 12))
                .build();
        filmController.addFilm(film);
        assertEquals(1, filmController.getAllFilms().size());
    }
}