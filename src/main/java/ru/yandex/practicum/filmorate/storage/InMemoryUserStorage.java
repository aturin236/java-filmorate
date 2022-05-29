package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage{
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        if (users.containsValue(user)) {
            throw new UserAlreadyExistException(String.format("Пользователь с логином %s уже добавлен",
                    user.getLogin()));
        }

        updateNameUser(user);

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (getUserById(user.getId()).isEmpty()) {
            throw new UserNotFoundException(String.format("Пользователь с id %s не найден",
                    user.getId()));
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    private void updateNameUser(User user) {
        if ((user.getName() == null) || (user.getName().trim().isEmpty())) {
            user.setName(user.getLogin());
        }
    }
}
