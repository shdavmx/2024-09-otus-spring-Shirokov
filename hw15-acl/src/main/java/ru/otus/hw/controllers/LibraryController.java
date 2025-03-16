package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.security.models.AuthorityNames;
import ru.otus.hw.security.models.LibraryRole;
import ru.otus.hw.security.models.LibraryUser;
import ru.otus.hw.security.services.LibraryUserService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class LibraryController {
    private final LibraryUserService libraryUserService;

    @GetMapping("/")
    public String getLibraryView() {
        return "library";
    }
}
