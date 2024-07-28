package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    public Film createFilm(Film film);
    public Film updateFilm(Film film);
    public Film removeFilm(Film film);
    public Film getFilmById(long filmId);
    public Collection<Film> getFilms();
    public boolean doesFilmExist(long filmId);
}
