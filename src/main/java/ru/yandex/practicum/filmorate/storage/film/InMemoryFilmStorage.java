package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
    final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        film.setId(getNextId());
        log.debug("Фильму был присвоен новый id {}", film.getId());
        films.put(film.getId(), film);
        log.info("Фильм с id {} был добавлен", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) throws NotFoundException{
        if (film.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        }
        throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
    }

    @Override
    public Film removeFilm(Film film) {
        return films.remove(film.getId());
    }

    @Override
    public Film getFilmById(long filmId) {
        return films.get(filmId);
    }

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public boolean doesFilmExist(long filmId) {
        return films.containsKey(filmId);
    }

    long getNextId() {
        long currentMaxID = films.values().stream()
                .mapToLong(Film::getId)
                .max()
                .orElse(0);
        return ++currentMaxID;
    }
}
