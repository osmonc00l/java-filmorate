package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidator;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getFilms() {
        log.info("Получен запрос на получение всех фильмов");
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {
        FilmValidator.isValid(film);
        try {
            log.info("Получен запрос на создание фильма: {}", film);
            return filmStorage.createFilm(film);
        } catch (Exception exception) {
            log.warn(exception.getMessage(), exception);
            throw exception;
        }
    }

    public Film updateFilm(Film film) {
        FilmValidator.isValid(film);
        try {
            if (Objects.isNull(film.getId())) {
                throw new ValidationException("Id должен быть указан");
            }
            Long id = film.getId();
            log.info("Получен запрос на обновление фильма: {}", film);
            Film oldFilm = filmStorage.getFilmById(id).orElseThrow(() -> new FilmNotFoundException(id));
            log.info("Фильм с id={} найден", film);
            oldFilm.setName(film.getName());
            oldFilm.setDescription(film.getDescription());
            oldFilm.setReleaseDate(film.getReleaseDate());
            oldFilm.setDuration(film.getDuration());
            oldFilm.setGenres(film.getGenres());
            oldFilm.setMpa(film.getMpa());
            log.info("Фильм успешно обновлен");
            return filmStorage.createFilm(oldFilm);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw exception;
        }
    }

    public void likeFilm(long filmId, long userId) {
        log.info("Пользователю {} понравился фильм {}", userId, filmId);
        userStorage.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        filmStorage.likeFilm(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        log.info("Пользователь {} хочет убрать фильм {} из любимых фильмов", userId, filmId);
        userStorage.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getMostPopularFilms(Optional<Long> count) {
        log.info("Запрос на получение популярных фильмов");
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(a -> -a.getLikes().size()))
                .limit(count.orElse(10L))
                .collect(Collectors.toList());
    }

    public Film getFilmById(Long id) {
        log.info("Запрос на получение фильма по ID {}", id);
        Optional<Film> film = filmStorage.getFilmById(id);
        return film.orElseThrow(() -> new FilmNotFoundException(id));
    }
}
