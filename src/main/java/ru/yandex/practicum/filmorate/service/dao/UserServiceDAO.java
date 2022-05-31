package ru.yandex.practicum.filmorate.service.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;


@Service("UserServiceDAO")
public class UserServiceDAO implements UserService {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    public UserServiceDAO(JdbcTemplate jdbcTemplate, @Qualifier("UserStorageDAO") UserStorage userStorage) {
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
    }

    public void deleteFriend(Long userId, Long friendId) {
        checkUserAvailability(userId);
        checkUserAvailability(friendId);
    }

    public Collection<User> getFriends(Long userId) {
        checkUserAvailability(userId);

        return null;
    }

    public Collection<User> getCommonFriends(Long userId, Long friendId) {
        checkUserAvailability(userId);
        checkUserAvailability(friendId);

        return null;
    }

    private void checkUserAvailability(Long id) {
        if (userStorage.getUserById(id).isEmpty()) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id=%s", id));
        }
    }
}
