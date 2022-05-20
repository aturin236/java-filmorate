package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        if (films.containsValue(film)) {
            throw new FilmAlreadyExistException(String.format("Фильм с наименованием %s уже добавлен", film.getName()));
        }

        films.put(film.getId(), film);
        return  film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (getFilmById(film.getId()).isEmpty()) {
            throw new FilmNotFoundException(String.format("Фильм с id %s не найден",
                    film.getId()));
        }

        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return Optional.ofNullable(films.get(id));
    }
}
