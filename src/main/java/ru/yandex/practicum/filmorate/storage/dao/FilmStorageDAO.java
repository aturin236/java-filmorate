package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Component("FilmStorageDAO")
@Slf4j
public class FilmStorageDAO implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmStorageDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Film addFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(FilmStorageSQL.insertFilm(), new String[]{"FilmId"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRating().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return film;
    }

    public Film updateFilm(Film film) {
        if (getFilmById(film.getId()).isEmpty()) {
            throw new FilmNotFoundException(String.format("Фильм с id %s не найден",
                    film.getId()));
        }
        jdbcTemplate.update(FilmStorageSQL.updateFilm()
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getRating().getId()
                , film.getId());

        return film;
    }

    public Collection<Film> getAllFilms() {
        String sqlQuery = FilmStorageSQL.selectFilmsSqlQuery();

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> FilmStorageSQL.makeFilm(rs));
    }

    public Optional<Film> getFilmById(Long id) {
        String sqlQuery = FilmStorageSQL.selectFilmsSqlQuery() + FilmStorageSQL.addWhereForSelectFilm();

        Film film = null;

        try {
            film = jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> FilmStorageSQL.makeFilm(rs), id);
        } catch (EmptyResultDataAccessException e) {
            log.info(e.getMessage());
        }

        return Optional.ofNullable(film);
    }
}
