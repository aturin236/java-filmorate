package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @NotBlank
    private String login;
    private String name;
    @EqualsAndHashCode.Exclude
    private long id;
    @Email
    private String email;
    @PastOrPresent
    private LocalDate birthday;
}
