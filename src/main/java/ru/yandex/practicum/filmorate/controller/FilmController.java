package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public ResponseEntity<Collection<FilmDto>> getFilms() {
        return ResponseEntity
                .status(200)
                .body(filmService.getFilms());
    }

    @PostMapping
    public ResponseEntity<FilmDto> createFilm(@Valid @RequestBody FilmDto filmDto) {
        return ResponseEntity
                .status(201)
                .body(filmService.createFilm(filmDto));
    }

    @PutMapping
    public ResponseEntity<FilmDto> updateFilm(@Valid @RequestBody FilmDto filmDto) {
        return ResponseEntity
                .status(200)
                .body(filmService.updateFilm(filmDto));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<?> likeFilm(@PathVariable long id, @PathVariable long userId) {
        log.debug("Получен запрос на создание лайка фильму с id={} пользователем с Id={}", id, userId);
        filmService.likeFilm(id, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<?> removeFilm(@PathVariable long id, @PathVariable long userId) {
        log.debug("Получен запрос на создание лайка фильму с id={} пользователем с Id={}", id, userId);
        filmService.removeLike(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmDto> getFilm(@PathVariable Long id) {
        log.debug("Получен запрос на получение фильма с ID: {}", id);
        return ResponseEntity
                .status(200)
                .body(filmService.getFilmById(id));
    }

    @GetMapping("/popular")
    public ResponseEntity<Collection<FilmDto>> getMostPopular(@RequestParam(defaultValue = "10") int count) {
        return ResponseEntity
                .status(200)
                .body(filmService.getMostPopularFilms(count));
    }
}
