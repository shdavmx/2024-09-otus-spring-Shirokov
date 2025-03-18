package ru.otus.hw.services;

import ru.otus.hw.models.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> findAll();

    AuthorDto findById(Long id);

    AuthorDto insert(String fullName);

    AuthorDto update(Long id, String fullName);

    void deleteById(Long id);
}
