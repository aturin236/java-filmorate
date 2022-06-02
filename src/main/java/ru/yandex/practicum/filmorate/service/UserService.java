package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

public interface UserService extends UserStorage {
    void addFriend(Long userId, Long friendId);
    void deleteFriend(Long userId, Long friendId);
    Collection<User> getFriends(Long userId);
    Collection<User> getCommonFriends(Long userId, Long friendId);
}
