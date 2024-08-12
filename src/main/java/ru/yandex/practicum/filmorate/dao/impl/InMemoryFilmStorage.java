package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        film.setId(getNextId());
        log.info("Фильму был присвоен новый id {}", film.getId());
        films.put(film.getId(), film);
        log.info("Фильм с id {} был добавлен", film.getId());
        return film;


    }

    public Film removeFilm(Film film) {
        log.info("Удаление фильма {}", film);
        return films.remove(film.getId());
    }

    @Override
    public Optional<Film> getFilmById(long filmId) {
        log.info("Поиск фильма по id: {}", filmId);
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public Collection<Film> getFilms() {
        log.info("Получение списка всех фильмов");
        return films.values();
    }

    public void likeFilm(long id, long userId) {
        getFilmById(id)
                .orElseThrow(() -> new FilmNotFoundException(id))
                .getLikes()
                .add(userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        getFilmById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(filmId))
                .getLikes()
                .remove(userId);
    }

    private long getNextId() {
        long currentMaxID = films.values().stream()
                .mapToLong(Film::getId)
                .max()
                .orElse(0);
        return ++currentMaxID;
    }
}
