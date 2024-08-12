package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;
    private final String likePath = "/{id}/like";

    @GetMapping
    public ResponseEntity<Collection<Film>> getFilms() {
        return ResponseEntity
                .status(200)
                .body(filmService.getFilms());
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) {
        return ResponseEntity
                .status(201)
                .body(filmService.createFilm(film));
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        return ResponseEntity
                .status(200)
                .body(filmService.updateFilm(film));
    }

    @PutMapping(likePath + "/{userId}")
    public ResponseEntity<?> likeFilm(@PathVariable long id, @PathVariable long userId) {
        filmService.likeFilm(id, userId);
        return ResponseEntity
                .status(200)
                .build();
    }

    @DeleteMapping(likePath + "/{userId}")
    public ResponseEntity<?> removeFilm(@PathVariable long id, @PathVariable long userId) {
        filmService.removeLike(id, userId);
        return ResponseEntity
                .status(200)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilm(@PathVariable Long id) {
        log.debug("Получен запрос на получение фильма с ID: {}", id);
        return ResponseEntity
                .status(200)
                .body(filmService.getFilmById(id));
    }

    @GetMapping("/popular")
    @ResponseBody
    public ResponseEntity<List<Film>> getMostPopular(@RequestParam Optional<Long> count) {
        return ResponseEntity
                .status(200)
                .body(filmService.getMostPopularFilms(count));
    }
}
