package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    public CommentDto findById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isPresent()) {
            return CommentDto.fromDomainObject(comment.get());
        }
        throw new EntityNotFoundException("Comment with id '%s' not found".formatted(id));
    }

    @Override
    public List<CommentDto> findAllByBookId(Long bookId) {
        return commentRepository.findAllByBookId(bookId).stream()
                .map(CommentDto::fromDomainObject)
                .toList();
    }

    @Override
    public CommentDto insert(String commentText, Long bookId) {
        return save(null, commentText, bookId);
    }

    @Override
    public CommentDto update(Long id, String commentText, Long bookId) {
        return save(id, commentText, bookId);
    }

    @Override
    public void deleteById(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new EntityNotFoundException("Comment with id '%s' not found".formatted(id));
        }

        commentRepository.deleteById(id);
    }

    private CommentDto save(Long id, String commentText, Long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Book with id '%s' not found".formatted(bookId));
        }

        Comment comment = new Comment(id, commentText, book.get());
        return CommentDto.fromDomainObject(commentRepository.save(comment));
    }
}
