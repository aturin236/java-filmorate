package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

@Data
public class RatingMPAA {
    @NonNull
    @EqualsAndHashCode.Exclude
    private int id;
    private String name;
    private String description;
}
