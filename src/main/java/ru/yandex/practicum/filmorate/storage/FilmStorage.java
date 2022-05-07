package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Film addFilm(Film film) throws ValidationException;

    Film updateFilm(Film film) throws ValidationException;

    Collection<Film> getAllFilms();

    Optional<Film> getFilmById(Long id);
}
