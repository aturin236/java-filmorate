package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.service.IdGenerator;
import ru.yandex.practicum.filmorate.model.service.ValidationException;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private long id;
    private String name;
    @NotBlank
    private String login;
    @PastOrPresent
    private LocalDate birthday;
    @Email
    private String email;

    public User(String name, String login, LocalDate birthday, String email) {
        this.id = IdGenerator.nextId();
        this.name = name;
        this.login = login;
        this.birthday = birthday;
        this.email = email;
    }
}
