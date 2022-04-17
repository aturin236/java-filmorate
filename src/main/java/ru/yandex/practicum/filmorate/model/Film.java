package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.service.IdGenerator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {
    @NotBlank
    private String name;
    private long id = IdGenerator.nextId();
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private short duration;
}
