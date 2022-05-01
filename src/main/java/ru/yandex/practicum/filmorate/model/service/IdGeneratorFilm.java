package ru.yandex.practicum.filmorate.model.service;

public class IdGeneratorFilm {
    private static long id = 0;

    public static synchronized long nextId() {
        return ++id;
    }
}
