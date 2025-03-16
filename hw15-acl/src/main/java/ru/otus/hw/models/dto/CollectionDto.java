package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Collection;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Data
public class CollectionDto {
    private Long id;

    private String name;

    private String description;

    private List<BookDto> books;

    @Override
    public String toString() {
        String booksString = "No one book is in collection";
        if (books != null && !books.isEmpty()) {
            booksString = books.stream()
                    .map(BookDto::toString)
                    .map("[%s]"::formatted)
                    .collect(Collectors.joining(";"));
        }

        return "Collection '%s' has books: '%s'".formatted(name, booksString);
    }

    public Collection toDomainObject() {
        List<Book> dbBook = null;
        if (books != null && !books.isEmpty()) {
            dbBook = books.stream()
                    .map(BookDto::toDomainObject)
                    .toList();
        }

        return new Collection(id, name, description, dbBook);
    }

    public static CollectionDto fromDomainObject(Collection collection) {
        List<BookDto> books = null;
        if (collection.getBooks() != null && !collection.getBooks().isEmpty()) {
            books = collection.getBooks().stream()
                    .map(BookDto::fromDomainObject)
                    .toList();
        }

        return new CollectionDto(collection.getId(), collection.getName(),
                collection.getDescription(), books);
    }
}
