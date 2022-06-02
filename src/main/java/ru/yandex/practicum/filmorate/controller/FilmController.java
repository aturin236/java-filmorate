package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.dto.FilmDTO;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(@Qualifier("FilmServiceDAO") FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public FilmDTO addFilm(@Valid @RequestBody FilmDTO filmDTO) throws ValidationException {
        log.debug("Запрос на добавление фильма - {}", filmDTO.getName());

        Film film = filmService.addFilm(FilmDTO.FilmDTOToFilm(filmDTO));

        return FilmDTO.FilmToFilmDTO(film);
    }

    @PutMapping
    public FilmDTO updateFilm(@Valid @RequestBody FilmDTO filmDTO) throws ValidationException {
        log.debug("Запрос на обновление фильма c id- {}", filmDTO.getId());

        Film film = filmService.updateFilm(FilmDTO.FilmDTOToFilm(filmDTO));

        return FilmDTO.FilmToFilmDTO(film);
    }

    @GetMapping
    public Collection<FilmDTO> getAllFilms() {
        return filmService.getAllFilms().stream()
                .map(FilmDTO::FilmToFilmDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public FilmDTO getFilmById(@PathVariable Long id) {
        Optional<Film> film = filmService.getFilmById(id);
        if (film.isEmpty()) {
            throw new FilmNotFoundException(String.format("Не найден фильм с id=%s", id));
        }
        return FilmDTO.FilmToFilmDTO(film.get());
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(
            @PathVariable Long id,
            @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(
            @PathVariable Long id,
            @PathVariable Long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<FilmDTO> getTopFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) Byte count) {
        return filmService.getMostPopularFilms(count).stream()
                .map(FilmDTO::FilmToFilmDTO)
                .collect(Collectors.toList());
    }
}
