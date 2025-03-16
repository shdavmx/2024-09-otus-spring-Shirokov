package ru.otus.hw.services;

import ru.otus.hw.models.dto.GenreDto;

import java.util.List;
import java.util.Set;

public interface GenreService {
    GenreDto findById(Long id);

    List<GenreDto> findAllByIds(Set<Long> ids);

    List<GenreDto> findAll();

    GenreDto insert(String name);

    GenreDto update(Long id, String name);

    void deleteById(Long id);
}
