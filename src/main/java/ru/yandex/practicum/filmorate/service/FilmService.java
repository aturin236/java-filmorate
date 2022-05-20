package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final Map<Long, Set<Long>> filmsLikes = new HashMap<>();

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) throws ValidationException {
        Film addedFilm = filmStorage.addFilm(film);

        if (!filmsLikes.containsKey(film.getId())) {
            filmsLikes.put(film.getId(), new HashSet<>());
        }

        return addedFilm;
    }

    public Film updateFilm(Film film) throws ValidationException {
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Optional<Film> getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public void addLike(Long filmId, Long userId) {
        checkUserAvailability(userId);
        checkFilmAvailability(filmId);

        Set<Long> filmLikes = filmsLikes.getOrDefault(filmId, new HashSet<>());
        filmLikes.add(userId);
        filmsLikes.put(filmId, filmLikes);
    }

    public void deleteLike(Long filmId, Long userId) {
        checkUserAvailability(userId);
        checkFilmAvailability(filmId);

        Set<Long> filmLikes = filmsLikes.get(filmId);

        if (filmLikes == null) return;

        filmLikes.remove(userId);

        filmsLikes.put(filmId, filmLikes);
    }

    public Collection<Film> getMostPopularFilms(Byte count) {
        return filmsLikes.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparing(x -> 1 - x.size())))
                .limit(count)
                .map(x -> filmStorage.getFilmById(x.getKey()).get())
                .collect(Collectors.toList());
    }

    private void checkUserAvailability(Long id) {
        if (userStorage.getUserById(id).isEmpty()) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id=%s", id));
        }
    }

    private void checkFilmAvailability(Long id) {
        if (filmStorage.getFilmById(id).isEmpty()) {
            throw new FilmNotFoundException(String.format("Не найден фильм с id=%s", id));
        }
    }
}
