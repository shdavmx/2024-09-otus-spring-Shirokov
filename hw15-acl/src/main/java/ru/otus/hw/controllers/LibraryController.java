package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.security.services.LibraryUserService;

@RequiredArgsConstructor
@Controller
public class LibraryController {
    private final LibraryUserService libraryUserService;

    @GetMapping("/")
    public String getLibraryView() {
        return "library";
    }
}
