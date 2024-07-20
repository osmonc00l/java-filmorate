package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        String isValid = checkIfValid(film);
        if (!isValid.isEmpty()) {throw new ValidationException(isValid);}
        log.info("Валидация прошла успешно");
        film.setId(getNextId());
        log.debug("Фильму был присвоен новый id {}", film.getId());
        films.put(film.getId(), film);
        log.info("Фильм с id {} был добавлен", film.getId());
        return film;
    }

    @PutMapping
    public Film updateUser(@RequestBody Film newFilm) {
        // проверяем необходимые условия
        if (newFilm.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        String isValid = checkIfValid(newFilm);
        if (!isValid.isEmpty()) {
            throw new ValidationException(isValid);
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            oldFilm.setName(newFilm.getName());
            log.debug("Фильм {} поменял название на {}", newFilm.getId(), newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            log.debug("Фильм {} поменял описание на {}", newFilm.getId(), newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            log.debug("Фильм {} поменял дату релиза на {}", newFilm.getId(), newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.debug("Фильм {} поменял продолжительность на {}", newFilm.getId(), newFilm.getDuration());
            return oldFilm;
        }
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    public String checkIfValid(Film film) {
        if (Objects.isNull(film.getName()) || film.getName().isBlank()) {
            return "Название не может быть пустым";
        } else if (film.getDescription().length() > 200){
            return "Максимальная длина описания — 200 символов";
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            return "Дата релиза — не раньше 28 декабря 1895 года;";
        } else if (film.getDuration() <= 0) {
            return "Продолжительность фильма должна быть положительным числом.";
        } else {
            return "";
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
