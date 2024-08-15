package ru.yandex.practicum.filmorate.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface FilmMapper {
    FilmMapper INSTANCE = Mappers.getMapper(FilmMapper.class);

    Film toEntity(FilmDto filmDto);

    Collection<Film> toEntity(List<FilmDto> filmDtoList);

    FilmDto toDto(Film film);

    Collection<FilmDto> toDto(List<Film> filmList);
}
