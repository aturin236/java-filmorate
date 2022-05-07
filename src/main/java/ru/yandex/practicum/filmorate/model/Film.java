package ru.yandex.practicum.filmorate.model;

import lombok.*;

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
    @EqualsAndHashCode.Exclude
    private long id;
    @Size(max = 200)
    @NotBlank
    private String description;
    private LocalDate releaseDate;
    @Positive
    private short duration;
}
