package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.models.CollectionFormModel;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.CollectionDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CollectionService;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class CollectionController {
    private final BookService bookService;

    private final CollectionService collectionService;

    @GetMapping("/collections")
    public String getCollections(Model model) {
        List<CollectionDto> collections = collectionService.findAll();
        model.addAttribute("collections", collections);
        return "collection";
    }

    @GetMapping("/collections/edit/{id}")
    public String getEditCollection(@PathVariable("id") Long id, Model model) {
        List<BookDto> books = bookService.findAll();
        CollectionFormModel collection = new CollectionFormModel();
        if (id == 0) {
            collection.setName("collection");
            collection.setBookIds(books.stream().map(BookDto::getId).toList());
        } else {
            CollectionDto collectionDto = collectionService.findById(id);
            collection.setId(collectionDto.getId());
            collection.setName(collectionDto.getName());
            collection.setDescription(collectionDto.getDescription());
            collection.setBookIds(collectionDto.getBooks().stream().map(BookDto::getId).toList());
        }

        model.addAttribute("collection", collection);
        model.addAttribute("books", books);
        return "collection-edit";
    }

    @PostMapping("/collections/edit")
    public String addCollection(@Valid CollectionFormModel collection, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "collection-edit";
        }

        if (collection.getId() == null || collection.getId() == 0) {
            collectionService.insert(collection.getName(), collection.getDescription(),
                    collection.getBookIds());
        } else {
            List<BookDto> books = bookService.findAllBooksById(new HashSet<>(collection.getBookIds()));
            CollectionDto collectionDto = new CollectionDto();
            collectionDto.setId(collection.getId());
            collectionDto.setName(collection.getName());
            collectionDto.setDescription(collection.getDescription());
            collectionDto.setBooks(books);

            collectionService.update(collectionDto);
        }

        return "collection";
    }
}
