package ru.yandex.practicum.filmorate.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreMapper INSTANCE = Mappers.getMapper(GenreMapper.class);

    GenreDto toDto(Genre genre);

    List<GenreDto> toDto(List<Genre> genres);

    Genre toEntity(GenreDto genreDto);

    List<Genre> toEntity(List<GenreDto> genresDto);
}
