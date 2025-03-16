package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public GenreDto findById(Long id) {
        Optional<Genre> genre = genreRepository.findById(id);
        if (genre.isPresent()) {
            return GenreDto.fromDomainObject(genre.get());
        }
        throw new EntityNotFoundException("Genre '%s' not found".formatted(id));
    }

    @Override
    public List<GenreDto> findAllByIds(Set<Long> ids) {
        return genreRepository.findAllById(ids).stream()
                .map(GenreDto::fromDomainObject)
                .toList();
    }

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(GenreDto::fromDomainObject)
                .toList();
    }

    @Override
    public GenreDto insert(String name) {
        return GenreDto.fromDomainObject(genreRepository.save(new Genre(0, name)));
    }

    @Override
    public GenreDto update(Long id, String name) {
        return GenreDto.fromDomainObject(genreRepository.save(new Genre(id, name)));
    }

    @Override
    public void deleteById(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new EntityNotFoundException("Genre with id '%s' not found".formatted(id));
        }

        genreRepository.deleteById(id);
    }
}
