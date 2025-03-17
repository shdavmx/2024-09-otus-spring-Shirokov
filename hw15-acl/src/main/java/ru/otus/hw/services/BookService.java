package ru.otus.hw.services;

import ru.otus.hw.models.dto.BookDto;

import java.util.List;
import java.util.Set;

public interface BookService {
    BookDto findById(Long id);

    List<BookDto> findAll();

    List<BookDto> findAllByAuthorId(Long authorId);

    BookDto insert(String title, Long authorId, List<Long> genresIds);

    BookDto update(Long id, String title, Long authorId, List<Long> genresIds);

    void deleteById(Long id);

    List<BookDto> findAllBooksById(Set<Long> ids);
}
