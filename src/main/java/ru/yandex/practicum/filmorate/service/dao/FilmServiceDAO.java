package ru.yandex.practicum.filmorate.service.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dao.FilmStorageSQL;

import java.util.Collection;
import java.util.Optional;

@Service("FilmServiceDAO")
public class FilmServiceDAO implements FilmService {
    private final JdbcTemplate jdbcTemplate;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;


    public FilmServiceDAO(JdbcTemplate jdbcTemplate,
                          @Qualifier("FilmStorageDAO") FilmStorage filmStorage,
                          @Qualifier("UserStorageDAO") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) throws ValidationException {
        return filmStorage.addFilm(film);
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

        String sqlQuery = "MERGE INTO FilmLikes KEY (FilmID, UserID) values (?, ?)";
        jdbcTemplate.update(sqlQuery
                , filmId
                , userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        checkUserAvailability(userId);
        checkFilmAvailability(filmId);

        String sqlQuery = "DELETE FROM FilmLikes WHERE FilmID=? AND UserID=?";
        jdbcTemplate.update(sqlQuery
                , filmId
                , userId);
    }

    public Collection<Film> getMostPopularFilms(Byte count) {
        return jdbcTemplate.query(FilmStorageSQL.mostPopularFilmsQuery(),
                (rs, rowNum) -> FilmStorageSQL.makeFilm(rs), count);
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
