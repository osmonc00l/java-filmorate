package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.Valid;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class FilmDto {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private List<GenreDto> genres;
    private MpaDto mpa = new MpaDto();
    private Set<Long> likes = new HashSet<>();

    public @Valid List<GenreDto> getGenres() {
        return genres == null ? Collections.emptyList() : genres;
    }
}