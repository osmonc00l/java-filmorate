package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.mappers.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public List<MpaDto> findAll() {
        return MpaMapper.INSTANCE.toDto(mpaStorage.findAll());
    }

    public MpaDto findById(int id) {
        Mpa mpa = mpaStorage.findById(id).orElseThrow(() -> new MpaNotFoundException(id));
        return MpaMapper.INSTANCE.toDto(mpa);
    }
}
