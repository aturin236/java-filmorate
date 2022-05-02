package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{
    public static final LocalDate RELEASE_DATE_MIN = LocalDate.of(1895, 12, 28);

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) throws ValidationException {
        validate(film);

        films.put(film.getId(), film);

        return  film;
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        validate(film);

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

    private void validate(Film film) throws ValidationException {
        if (((film.getName() == null)) || (film.getName().isBlank())) {
            log.debug("Ошибка валидации фильма - {}", film.getName());
            throw new ValidationException("Наименование должно быть заполнено");
        }
        if (film.getDescription().length() > 200) {
            log.debug("Ошибка валидации фильма - {}", film.getName());
            throw new ValidationException("Длина описания не должна превышать 200 символов");
        }
        if (film.getReleaseDate().isBefore(RELEASE_DATE_MIN)) {
            log.debug("Ошибка валидации фильма - {}", film.getName());
            throw new ValidationException("Дата фильма меньше " + RELEASE_DATE_MIN);
        }
        if (film.getDuration() < 0) {
            log.debug("Ошибка валидации фильма - {}", film.getName());
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        if (films.containsValue(film)) {
            throw new FilmAlreadyExistException(String.format("Фильм с наименованием %s уже добавлен", film.getName()));
        }
    }
}
