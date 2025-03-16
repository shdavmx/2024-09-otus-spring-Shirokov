package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.CollectionDto;
import ru.otus.hw.repositories.CollectionRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CollectionServiceImpl implements CollectionService {
    private final CollectionRepository collectionRepository;

    @Override
    public List<CollectionDto> findAll() {
        return collectionRepository.findAll().stream()
                .map(CollectionDto::fromDomainObject)
                .toList();
    }

    @Override
    public void save(CollectionDto collectionDto) {

    }

    @Override
    public CollectionDto addBookToCollection(Long collectionId, BookDto bookDto) {
        return null;
    }

    @Override
    public CollectionDto removeBookFromCollection(Long collectionId, BookDto bookDto) {
        return null;
    }

    @Override
    public void removeCollectionById(Long collectionId) {

    }
}
