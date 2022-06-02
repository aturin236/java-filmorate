package ru.yandex.practicum.filmorate.storage.factory;

import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryFilmStorage;

public class FilmStorageFactory {
    public static FilmStorage getDefault() {
        return new InMemoryFilmStorage();
    }
}
