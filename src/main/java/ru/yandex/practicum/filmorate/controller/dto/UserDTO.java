package ru.yandex.practicum.filmorate.controller.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.service.IdGeneratorUser;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class UserDTO {
    @NotBlank
    private String login;
    private String name;
    @EqualsAndHashCode.Exclude
    private long id;
    @Email
    private String email;
    @PastOrPresent
    private LocalDate birthday;

    public static User UserDTOToUser(UserDTO userDTO) throws ValidationException {
        validate(userDTO);

        User user = User.builder()
                .login(userDTO.getLogin())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .birthday(userDTO.getBirthday())
                .build();

        if (userDTO.getId() != 0) {
            user.setId(userDTO.getId());
        } else {
            user.setId(IdGeneratorUser.nextId());
        }

        return user;
    }

    public static UserDTO UserToUserDTO(User user) {

        return UserDTO.builder()
                .login(user.getLogin())
                .name(user.getName())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .id(user.getId())
                .build();
    }

    private static void validate(UserDTO user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.debug("Ошибка валидации пользователя - {}", user.getLogin());
            throw new ValidationException("Почта не соответствует формату email");
        }
        if (user.getBirthday().isAfter(LocalDateTime.now().toLocalDate())) {
            log.debug("Ошибка валидации пользователя - {}", user.getLogin());
            throw new ValidationException("Дата рождения находится в будущем");
        }
        if (StringUtils.containsWhitespace(user.getLogin()) || user.getLogin().isBlank()) {
            log.debug("Ошибка валидации пользователя - {}", user.getLogin());
            throw new ValidationException("Логин содержит пробелы или пустой");
        }
    }
}
