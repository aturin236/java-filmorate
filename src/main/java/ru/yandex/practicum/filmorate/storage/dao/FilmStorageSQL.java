package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMPAA;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmStorageSQL {
    public static String insertFilm() {
        return "INSERT INTO Films(Name, Description, ReleaseDate, Duration, Rating)" +
                " values (?, ?, ?, ?, ?)";
    }

    public static String updateFilm() {
        return "UPDATE Films SET Name = ?, Description = ?, ReleaseDate = ?," +
                " Duration = ?, Rating = ? WHERE FilmID = ?";
    }

    public static Film makeFilm(ResultSet rs) throws SQLException {
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

    public static String selectFilmsSqlQuery() {
        return "SELECT\n" +
                "    F.FilmID AS ID,\n" +
                "    F.Name AS Name,\n" +
                "    F.Description AS Description,\n" +
                "    F.ReleaseDate AS ReleaseDate,\n" +
                "    F.Duration AS Duration,\n" +
                "    F.Rating AS Rating,\n" +
                "    RM.Name AS RatingName,\n" +
                "    RM.Description AS RatingDescription\n" +
                "FROM Films AS F\n" +
                "LEFT JOIN RatingMPAA RM on RM.RatingID = F.Rating";
    }

    public static String addWhereForSelectFilm() {
        return " WHERE FilmID = ?";
    }

    public static String mostPopularFilmsQuery() {
        return "WITH src as (\n" +
                "    SELECT filmID, COUNT(USERID) AS countLikes\n" +
                "    FROM FilmLikes\n" +
                "    GROUP BY filmID\n" +
                "    union\n" +
                "    SELECT filmID, 0\n" +
                "    From FILMS\n" +
                "    WHERE FILMID NOT IN (SELECT distinct FILMID from FILMLIKES)\n" +
                ")\n" +
                "SELECT\n" +
                "    F.FilmID AS ID,\n" +
                "    F.Name AS Name,\n" +
                "    F.Description AS Description,\n" +
                "    F.ReleaseDate AS ReleaseDate,\n" +
                "    F.Duration AS Duration,\n" +
                "    F.Rating AS Rating,\n" +
                "    RM.Name AS RatingName,\n" +
                "    RM.Description AS RatingDescription\n" +
                "FROM Films AS F\n" +
                "         LEFT JOIN RatingMPAA RM on RM.RatingID = F.Rating WHERE F.FilmID IN\n" +
                "                                                                 (SELECT filmID\n" +
                "                                                                  FROM src\n" +
                "                                                                  ORDER BY countLikes desc\n" +
                "                                                                    LIMIT ?\n" +
                "                                                                     )";
    }
}
