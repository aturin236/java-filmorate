package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMPAA;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        String sqlQuery = "INSERT INTO \"Films\"(\"Name\", \"Description\", \"ReleaseDate\", \"Duration\", \"Rating\")" +
                " values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FilmId"});
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
        String sqlQuery = "UPDATE \"Films\" SET \"Name\" = ?, \"Description\" = ?, \"ReleaseDate\" = ?," +
                " \"Duration\" = ?, \"Rating\" = ? WHERE \"FilmID\" = ?";

        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getRating().getId()
                , film.getId());

        return film;
    }

    public Collection<Film> getAllFilms() {
        String sqlQuery = selectFilmsSqlQuery();

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
    }

    public Optional<Film> getFilmById(Long id) {
        String sqlQuery = selectFilmsSqlQuery() + addWhereForSelectFilm();

        return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeFilm(rs), id));
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film newFilm = Film.builder()
                .id(rs.getInt("FilmID"))
                .name(rs.getString("Name"))
                .description(rs.getString("Description"))
                .releaseDate(rs.getDate("ReleaseDate").toLocalDate())
                .duration(rs.getShort("Duration"))
                .build();

        int ratingId = rs.getInt("Rating");
        if (ratingId != 0) {
            newFilm.setRating(new RatingMPAA(ratingId
                    , rs.getString("RatingName")
                    , rs.getString("RatingDescription")));
        }

        return newFilm;
    }

    private String selectFilmsSqlQuery() {
        return "SELECT\n" +
                "    F.\"FilmID\" AS ID,\n" +
                "    F.\"Name\" AS Name,\n" +
                "    F.\"Description\" AS Description,\n" +
                "    F.\"ReleaseDate\" AS ReleaseDate,\n" +
                "    F.\"Duration\" AS Duration,\n" +
                "    F.\"Rating\" AS Rating,\n" +
                "    RM.\"Name\" AS RatingName,\n" +
                "    RM.\"Description\" AS RatingDescription\n" +
                "FROM \"Films\" AS F\n" +
                "LEFT JOIN \"RatingMPAA\" RM on RM.\"RatingID\" = F.\"Rating\"";
    }

    private String addWhereForSelectFilm() {
        return " WHERE \"FilmID\" = ?";
    }
}
