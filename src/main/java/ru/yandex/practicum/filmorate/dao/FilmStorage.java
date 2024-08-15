package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    public Film createFilm(Film film);

    public Film updateFilm(Film film);

    public Optional<Film> getFilmById(Long filmId);

    public List<Film> getFilms();

    public void likeFilm(long id, long userId);

    public void removeLike(long filmId, long useId);
}
