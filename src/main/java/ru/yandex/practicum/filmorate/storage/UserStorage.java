package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.service.ValidationException;

import java.util.Collection;

public interface UserStorage {
    User addUser(User user) throws ValidationException;
    User updateUser(User user) throws ValidationException;
    Collection<User> getAllUsers();

}
