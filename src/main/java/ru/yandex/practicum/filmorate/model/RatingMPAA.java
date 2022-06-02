package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingMPAA {
    @NonNull
    @EqualsAndHashCode.Exclude
    private int id;
    private String name;
    private String description;
}
