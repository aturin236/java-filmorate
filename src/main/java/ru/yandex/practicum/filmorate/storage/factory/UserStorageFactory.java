package ru.yandex.practicum.filmorate.storage.factory;

import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

public class UserStorageFactory {
    public static UserStorage getDefault() {
        return new InMemoryUserStorage();
    }
}
