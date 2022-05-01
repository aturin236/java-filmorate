package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.service.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    public static final LocalDate RELEASE_DATE_MIN = LocalDate.of(1895, 12, 28);

    private final Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.debug("Запрос на добавление фильма - {}", film.getName());

        validate(film);

        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.debug("Запрос на обновление фильма c id- {}", film.getId());

        validate(film);

        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
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
    }
}
