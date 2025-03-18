package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.BookAlreadyExistInCollectionException;
import ru.otus.hw.exceptions.BookIsNotInCollectionException;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Collection;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.CollectionDto;
import ru.otus.hw.repositories.CollectionRepository;
import ru.otus.hw.security.services.AclWrapperService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class CollectionServiceImpl implements CollectionService {
    private final BookService bookService;

    private final AclWrapperService aclWrapperService;

    private final CollectionRepository collectionRepository;

    @PostFilter("hasPermission(filterObject, 'READ')")
    @Override
    public List<CollectionDto> findAll() {
        return collectionRepository.findAll().stream()
                .map(CollectionDto::fromDomainObject)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Override
    public CollectionDto insert(String name, String description, List<Long> bookIds) {
        List<BookDto> books = bookService.findAllBooksById(new HashSet<>(bookIds));
        List<Book> dbBooks = books.stream().map(BookDto::toDomainObject).toList();
        Collection collection = new Collection(null, name, description, dbBooks);
        Collection savedCollection = collectionRepository.save(collection);
        CollectionDto collectionDto = CollectionDto.fromDomainObject(savedCollection);

        aclWrapperService.createAllPermission(collectionDto);

        return collectionDto;
    }

    @Transactional
    @PreAuthorize("hasPermission(#collectionDto, 'WRITE')")
    @Override
    public CollectionDto update(CollectionDto collectionDto) {
        Collection savedCollection = collectionRepository.save(collectionDto.toDomainObject());
        return CollectionDto.fromDomainObject(savedCollection);
    }

    @Transactional
    @Override
    public CollectionDto addBookToCollection(Long collectionId, BookDto bookDto) {
        Optional<Collection> collection = collectionRepository.findById(collectionId);
        if (collection.isEmpty()) {
           throw new EntityNotFoundException("Collection with id '%d' not found".formatted(collectionId));
        }

        Book book = bookDto.toDomainObject();
        Collection dbCollection = collection.get();

        List<Book> books = dbCollection.getBooks();
        if (books == null || books.isEmpty()) {
            books = new ArrayList<>();
        }

        Stream<Book> existingBook = books.stream().filter(b -> b.getId() == book.getId());
        if (existingBook.findAny().isPresent()) {
            throw new BookAlreadyExistInCollectionException("Book '%s' already exist in collection '%s'"
                    .formatted(book.getTitle(), dbCollection.getName()));
        }

        books.add(book);
        dbCollection.setBooks(books);

        return update(CollectionDto.fromDomainObject(dbCollection));
    }

    @Transactional
    @Override
    public CollectionDto removeBookFromCollection(Long collectionId, BookDto bookDto) {
        CollectionDto collection = findById(collectionId);
        List<BookDto> books = collection.getBooks();

        Stream<BookDto> existingBook = books.stream().filter(b -> Objects.equals(b.getId(), bookDto.getId()));
        if (existingBook.findAny().isEmpty()) {
            throw new BookIsNotInCollectionException("Book '%s' is not in collection '%s'"
                    .formatted(bookDto.getTitle(), collection.getName()));
        }

        books = books.stream().filter(b -> !Objects.equals(b.getId(), bookDto.getId())).toList();
        collection.setBooks(books);

        return update(collection);
    }

    @Transactional
    @PreAuthorize("hasPermission(#collectionId, T(ru.otus.hw.models.dto.CollectionDto), 'DELETE')")
    @Override
    public void removeCollectionById(Long collectionId) {
        collectionRepository.deleteById(collectionId);
    }

    @PreAuthorize("canRead(#collectionId, T(ru.otus.hw.models.dto.CollectionDto))")
    @Override
    public CollectionDto findById(Long collectionId) {
        Optional<Collection> collection = collectionRepository.findById(collectionId);
        if (collection.isEmpty()) {
            throw new EntityNotFoundException("Collection with id '%d' not found".formatted(collectionId));
        }

        Collection dbCollection = collection.get();
        if (dbCollection.getBooks() == null ||
            dbCollection.getBooks().isEmpty()) {
            dbCollection.setBooks(new ArrayList<>());
        }

        return CollectionDto.fromDomainObject(dbCollection);
    }
}
