package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.service.ValidationException;

import java.util.Collection;

public interface FilmStorage {
    Film addFilm(Film film) throws ValidationException;
    Film updateFilm (Film film) throws ValidationException;
    Collection<Film> getAllFilms();
}
