package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("UserStorageDAO")
public class UserStorageDAO implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserStorageDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
