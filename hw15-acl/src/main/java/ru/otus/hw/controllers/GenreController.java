package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/genres")
    public String getGenres(Model model) {
        List<GenreDto> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "genres";
    }

    @PostMapping("/genres/delete")
    public String deleteGenreById(@RequestParam("id") Long id) {
        genreService.deleteById(id);
        return "redirect:/genres";
    }

    @GetMapping("/genres/edit/{id}")
    public String getEditGenre(@PathVariable("id") Long id, Model model) {
        GenreDto genre = new GenreDto(0L, "New_Genre");
        if (id != 0) {
            genre = genreService.findById(id);
        }
        model.addAttribute("genre", genre);
        return "genre-edit";
    }

    @PostMapping("/genres/edit")
    public String saveGenre(@Valid GenreDto genre, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "genre-edit";
        }

        if (genre.getId() == 0) {
            genreService.insert(genre.getName());
        } else {
            genreService.update(genre.getId(), genre.getName());
        }

        return "redirect:/genres";
    }
}
