package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
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

    public void addLike(Long filmId, Long userId) {
        checkUserAvailability(userId);
        checkFilmAvailability(filmId);

        Set<Long> filmLikes = filmsLikes.getOrDefault(userId, new HashSet<>());
        filmLikes.add(userId);
        filmsLikes.put(userId, filmLikes);
    }

    public void deleteLike(Long filmId, Long userId) {
        checkUserAvailability(userId);
        checkFilmAvailability(filmId);

        Set<Long> filmLikes = filmsLikes.get(userId);

        if (filmLikes == null) return;

        filmLikes.remove(userId);
        filmsLikes.put(userId, filmLikes);
    }

    public Optional<Collection<Long>> getMostPopularFilms(Byte count) {
        List<Long> mostPopularFilms = filmsLikes.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparing(Set::size)))
                .limit(count)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return Optional.of(mostPopularFilms);
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
