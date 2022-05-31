package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Component("UserStorageDAO")
@Slf4j
public class UserStorageDAO implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserStorageDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User addUser(User user) {
        updateNameUser(user);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(UserStorageSQL.insertUser(), new String[]{"UserId"});
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        return user;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update(UserStorageSQL.updateUser()
                , user.getLogin()
                , user.getName()
                , user.getEmail()
                , user.getBirthday()
                , user.getId());

        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        String sqlQuery = UserStorageSQL.selectUsersSqlQuery();

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> UserStorageSQL.makeUser(rs));
    }

    @Override
    public Optional<User> getUserById(Long id) {
        String sqlQuery = UserStorageSQL.selectUsersSqlQuery() + UserStorageSQL.addWhereForSelectUser();

        User user = null;

        try {
            user = jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> UserStorageSQL.makeUser(rs), id);
        } catch (EmptyResultDataAccessException e) {
            log.info(e.getMessage());
        }

        return Optional.ofNullable(user);
    }

    private void updateNameUser(User user) {
        if ((user.getName() == null) || (user.getName().trim().isEmpty())) {
            user.setName(user.getLogin());
        }
    }
}
