package ru.yandex.practicum.filmorate.service.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserStorageSQL;

import java.util.Collection;
import java.util.Optional;


@Service("UserServiceDAO")
public class UserServiceDAO implements UserService {
    private static final String ADD_FRIEND_QUERY = "MERGE INTO Friends KEY (UserID, FriendID)" +
            " values (:userId, :friendId)";
    public static final String DELETE_FRIEND_QUERY = "DELETE FROM Friends WHERE UserID=:userId AND FriendID=:friendId";
    public static final String JOIN_FOR_GET_FRIENDS = " INNER JOIN Friends ON Users.UserID = Friends.FriendID" +
            " WHERE Friends.UserID = :userId";
    public static final String WHERE_FOR_COMMON_FRIENDS = " WHERE UserID IN\n" +
            "    (SELECT FriendID\n" +
            "     FROM Friends\n" +
            "     WHERE UserID = :userId\n" +
            "       AND FriendID <> :friendId\n" +
            "     UNION SELECT FriendID\n" +
            "     FROM Friends\n" +
            "     WHERE UserID = :friendId\n" +
            "       AND FriendID <> :userId)";
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    public UserServiceDAO(NamedParameterJdbcTemplate jdbcTemplate,
                          @Qualifier("UserStorageDAO") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    public User addUser(User user) throws ValidationException {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) throws ValidationException {
        return userStorage.updateUser(user);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public Optional<User> getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public void addFriend(Long userId, Long friendId) {
        checkUserAvailability(userId);
        checkUserAvailability(friendId);

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("userId", userId);
        paramSource.addValue("friendId", friendId);
        jdbcTemplate.update(ADD_FRIEND_QUERY, paramSource);
    }

    public void deleteFriend(Long userId, Long friendId) {
        checkUserAvailability(userId);
        checkUserAvailability(friendId);

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("userId", userId);
        paramSource.addValue("friendId", friendId);
        jdbcTemplate.update(DELETE_FRIEND_QUERY, paramSource);
    }

    public Collection<User> getFriends(Long userId) {
        checkUserAvailability(userId);

        String sqlQuery = UserStorageSQL.selectUsersSqlQuery() + JOIN_FOR_GET_FRIENDS;
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("userId", userId);
        return jdbcTemplate.query(sqlQuery, paramSource, (rs, rowNum) -> UserStorageSQL.makeUser(rs));
    }

    public Collection<User> getCommonFriends(Long userId, Long friendId) {
        checkUserAvailability(userId);
        checkUserAvailability(friendId);

        String sqlQuery = UserStorageSQL.selectUsersSqlQuery() + WHERE_FOR_COMMON_FRIENDS;
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("userId", userId);
        paramSource.addValue("friendId", friendId);
        return jdbcTemplate.query(sqlQuery, paramSource, (rs, rowNum) -> UserStorageSQL.makeUser(rs));
    }

    private void checkUserAvailability(Long id) {
        if (userStorage.getUserById(id).isEmpty()) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id=%s", id));
        }
    }
}
