package ru.yandex.practicum.filmorate.validation;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Objects;

@Slf4j
@UtilityClass
public class FilmValidator {
    public static void isValid(Film film) {
        if (Objects.isNull(film.getName()) || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (Objects.isNull(film.getDescription()) || film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года;");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
        log.info("Валидация фильма прошла успешно");
    }
}
