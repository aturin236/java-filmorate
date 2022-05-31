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
                .id(rs.getInt("Users.UserID"))
                .login(rs.getString("Users.Login"))
                .name(rs.getString("Users.Name"))
                .email(rs.getString("Users.Email"))
                .birthday(rs.getDate("Users.Birthday").toLocalDate())
                .build();
    }

    public static String selectUsersSqlQuery() {
        return "SELECT \"Users\".\"UserID\",\n" +
                "       \"Users\".\"Login\",\n" +
                "       \"Users\".\"Name\",\n" +
                "       \"Users\".\"Email\",\n" +
                "       \"Users\".\"Birthday\"\n" +
                "FROM \"Users\"";
    }

    public static String addWhereForSelectUser() {
        return " WHERE \"Users\".\"UserID\" = ?";
    }
}
