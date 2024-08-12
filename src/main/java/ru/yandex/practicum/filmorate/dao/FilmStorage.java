package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    public Film createFilm(Film film);

    public Optional<Film> getFilmById(long filmId);

    public Collection<Film> getFilms();

    public void likeFilm(long id, long userId);

    public void removeLike(long filmId, long useId);
}
