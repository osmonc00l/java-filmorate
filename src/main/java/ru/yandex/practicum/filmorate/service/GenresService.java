package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenresStorage;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenresService {
    private final GenresStorage genresStorage;

    public List<GenreDto> findAll() {
        return GenreMapper.INSTANCE.toDto(genresStorage.findAll());
    }

    public GenreDto findById(int id) {
        Genre genre = genresStorage.findById(id).orElseThrow(() -> new GenreNotFoundException(id));
        return GenreMapper.INSTANCE.toDto(genre);
    }
}
