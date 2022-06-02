package ru.yandex.practicum.filmorate.controller.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMPAA;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class FilmDTO {
    public static final LocalDate RELEASE_DATE_MIN = LocalDate.of(1895, 12, 28);

    @NotBlank
    private String name;
    @EqualsAndHashCode.Exclude
    private long id;
    @Size(max = 200)
    @NotBlank
    private String description;
    private LocalDate releaseDate;
    @Positive
    private short duration;
    private RatingMPAA mpa;

    public static Film FilmDTOToFilm(FilmDTO filmDTO) throws ValidationException {
        validate(filmDTO);

        Film film = Film.builder()
                .name(filmDTO.getName())
                .description(filmDTO.getDescription())
                .releaseDate(filmDTO.getReleaseDate())
                .duration(filmDTO.getDuration())
                .rating(filmDTO.getMpa())
                .build();

        if (filmDTO.getId() != 0) {
            film.setId(filmDTO.getId());
        }
        return film;
    }

    public static FilmDTO FilmToFilmDTO(Film film) {

        return FilmDTO.builder()
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .id(film.getId())
                .mpa(film.getRating())
                .build();
    }

    private static void validate(FilmDTO film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
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
