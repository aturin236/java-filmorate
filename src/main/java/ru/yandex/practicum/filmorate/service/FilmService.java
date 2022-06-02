package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

public interface FilmService extends FilmStorage {
    void addLike(Long filmId, Long userId);
    void deleteLike(Long filmId, Long userId);
    Collection<Film> getMostPopularFilms(Byte count);
}
