package ru.yandex.practicum.filmorate.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MpaMapper {
    MpaMapper INSTANCE = Mappers.getMapper(MpaMapper.class);

    Mpa toEntity(MpaDto mpaDto);

    MpaDto toDto(Mpa mpa);

    List<Mpa> toEntity(List<MpaDto> mpaDtoList);

    List<MpaDto> toDto(List<Mpa> mpaList);
}
