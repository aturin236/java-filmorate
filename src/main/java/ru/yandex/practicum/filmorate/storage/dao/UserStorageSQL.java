package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserStorageSQL {
    public static String insertUser() {
        return "INSERT INTO \"Users\"(\"Login\", \"Name\", \"Email\", \"Birthday\")" +
                " values (?, ?, ?, ?)";
    }

    public static String updateUser() {
        return "UPDATE \"Users\" SET \"Login\" = ?, \"Name\" = ?, \"Email\" = ?," +
                " \"Birthday\" = ? WHERE \"UserID\" = ?";
    }

    public static User makeUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getInt("UserID"))
                .login(rs.getString("Login"))
                .name(rs.getString("Name"))
                .email(rs.getString("Email"))
                .birthday(rs.getDate("Birthday").toLocalDate())
                .build();
    }

    public static String selectUsersSqlQuery() {
        return "SELECT * FROM \"Films\"";
    }

    public static String addWhereForSelectUser() {
        return " WHERE \"UserID\" = ?";
    }
}
