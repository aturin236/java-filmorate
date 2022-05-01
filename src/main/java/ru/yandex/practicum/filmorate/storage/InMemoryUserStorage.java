package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.service.ValidationException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User addUser(User user) throws ValidationException {
        validate(user);
        updateNameUser(user);

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        validate(user);

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    private void updateNameUser(User user) {
        if ((user.getName() == null) || (user.getName().trim().isEmpty())) {
            user.setName(user.getLogin());
        }
    }

    private void validate(User user) throws ValidationException {
        if ((user.getEmail().isBlank()) || (!user.getEmail().contains("@"))) {
            log.debug("Ошибка валидации пользователя - {}", user.getLogin());
            throw new ValidationException("Почта не соответствует формату email");
        }
        if (user.getBirthday().isAfter(LocalDateTime.now().toLocalDate())) {
            log.debug("Ошибка валидации пользователя - {}", user.getLogin());
            throw new ValidationException("Дата рождения находится в будущем");
        }
        if ((StringUtils.containsWhitespace(user.getLogin()) || (user.getLogin().isBlank()))) {
            log.debug("Ошибка валидации пользователя - {}", user.getLogin());
            throw new ValidationException("Логин содержит пробелы или пустой");
        }
    }
}
