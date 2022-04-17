package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.service.ValidationException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        log.debug("Запрос на добавление пользователя - {}", user.getLogin());

        validate(user);
        updateNameUser(user);

        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        log.debug("Запрос на обновление пользователя - {}", user.getLogin());

        validate(user);

        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    private void updateNameUser(User user) {
        if ((user.getName() == null) || (user.getName().trim().isEmpty())) {
            user.setName(user.getLogin());
        }
    }

    private void validate(User user) throws ValidationException {
        if (StringUtils.containsWhitespace(user.getLogin())) {
            log.debug("Ошибка валидации пользователя - {}", user.getLogin());
            throw new ValidationException("Логин содержит пробелы");
        }
    }
}
