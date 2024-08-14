package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.mappers.MpaMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.validation.FilmByLikeComparator;
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
    public Collection<FilmDto> getFilms() {
        log.info("Получен запрос на получение всех фильмов");
        return FilmMapper.INSTANCE.toDto(filmStorage.getFilms());
    }

    public FilmDto createFilm(FilmDto filmDto) {
        log.info("Получен запрос на создание фильма: {}", filmDto);
        Film film = FilmMapper.INSTANCE.toEntity(filmDto);
        FilmValidator.isValid(film);
        return FilmMapper.INSTANCE.toDto(filmStorage.createFilm(film));
    }

    public FilmDto updateFilm(FilmDto filmDto) {
        Long id = filmDto.getId();
        log.info("Получен запрос на обновление фильма: {}", filmDto);
        Film storedFilm = filmStorage.getFilmById(id).orElseThrow(() -> new FilmNotFoundException(id));
        log.info("Фильм с id={} найден", filmDto.getId());
        storedFilm.setName(filmDto.getName());
        storedFilm.setDescription(filmDto.getDescription());
        storedFilm.setReleaseDate(filmDto.getReleaseDate());
        storedFilm.setDuration(filmDto.getDuration());
        storedFilm.setMpa(MpaMapper.INSTANCE.toEntity(filmDto.getMpa()));
        log.info("Фильм успешно обновлен");
        return FilmMapper.INSTANCE.toDto(filmStorage.updateFilm(storedFilm));

    }

    public void likeFilm(long filmId, long userId) {
        log.info("Пользователю {} понравился фильм {}", userId, filmId);
        userStorage.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        log.info("Фильм найден");
        filmStorage.likeFilm(filmId, userId);
        log.info("Лайк добавлен");
    }

    public void removeLike(long filmId, long userId) {
        log.info("Пользователь {} хочет убрать фильм {} из любимых фильмов", userId, filmId);
        userStorage.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        filmStorage.removeLike(filmId, userId);
    }

    public Collection<FilmDto> getMostPopularFilms(int count) {
        log.info("Запрос на получение популярных фильмов");
        return filmStorage.getFilms().stream()
                .sorted(new FilmByLikeComparator().reversed())
                .limit(count)
                .map(FilmMapper.INSTANCE::toDto)
                .toList();
    }

    public FilmDto getFilmById(Long id) {
        log.info("Запрос на получение фильма по ID {}", id);
        Film film = filmStorage.getFilmById(id)
                .orElseThrow(() -> new FilmNotFoundException(id));
        return FilmMapper.INSTANCE.toDto(film);
    }
}
