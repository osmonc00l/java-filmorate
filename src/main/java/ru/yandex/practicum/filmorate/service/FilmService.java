package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidator;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {
        FilmValidator.isValid(film);
        try {
            return filmStorage.createFilm(film);
        } catch (Exception exception) {
            log.warn(exception.getMessage(), exception);
            throw exception;
        }
    }

    public Film updateFilm(Film film) {
        FilmValidator.isValid(film);
        try {
            return filmStorage.updateFilm(film);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw exception;
        }
    }

    public Film likeFilm(long filmId, long userId) {
        return editLike(filmId, userId, (likes) -> likes.add(userId));
    }

    public Film removeLike(long filmId, long userId) {
        return editLike(filmId, userId, (likes) -> likes.remove(userId));
    }

    public List<Film> getMostPopularFilms(Optional<Long> count) {
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(a -> -a.getLikes().size()))
                .limit(count.orElse(10L))
                .collect(Collectors.toList());
    }

    private Film editLike(long filmId, long userId, Consumer<Set<Long>> action) {
        if (!filmStorage.doesFilmExist(filmId)) {
            throw new NotFoundException(String.format("Фильм с ID=%d не найден", filmId));
        }
        if (!userStorage.doesUserExist(userId)) {
            throw new NotFoundException(String.format("Пользователь с ID=%d не найден", userId));
        }
        Film film = filmStorage.getFilmById(filmId);
        action.accept(film.getLikes());
        return film;
    }
}
