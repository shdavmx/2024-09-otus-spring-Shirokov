package ru.otus.hw.services;

import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.CollectionDto;

import java.util.List;

public interface CollectionService {
    List<CollectionDto> findAll();

    void save(CollectionDto collectionDto);

    CollectionDto addBookToCollection(Long collectionId, BookDto bookDto);

    CollectionDto removeBookFromCollection(Long collectionId, BookDto bookDto);

    void removeCollectionById(Long collectionId);
}
