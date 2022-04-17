package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.service.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    void createContext() {
        filmController = new FilmController();
    }

    @Test
    void Should_ThrowException_WhenReleaseDateOld() {
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

    @Test
    void Should_AddFilm_WithSuccess() throws ValidationException {
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