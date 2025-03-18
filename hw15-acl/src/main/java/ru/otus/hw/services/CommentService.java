package ru.otus.hw.services;

import ru.otus.hw.models.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto findById(Long id);

    List<CommentDto> findAllByBookId(Long bookId);

    CommentDto insert(String commentText, Long bookId);

    CommentDto update(Long id, String commentText, Long bookId);

    void deleteById(Long id);
}
