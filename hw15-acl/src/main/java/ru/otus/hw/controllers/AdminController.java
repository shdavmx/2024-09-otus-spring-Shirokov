package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.security.models.LibraryRole;
import ru.otus.hw.security.models.LibraryUser;
import ru.otus.hw.security.services.LibraryUserService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AdminController {
    private final LibraryUserService libraryUserService;

    @GetMapping("/admin")
    public String getUsers(Model model) {
        List<LibraryUser> users = libraryUserService.findAll();
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping("/admin/user/edit/{id}")
    public String getEditUser(@PathVariable("id") Long id, Model model) {
        LibraryUser user = new LibraryUser();
        user.setUsername("new_user");
        user.setUserAuthorities(List.of(new LibraryRole(null, "USER")));
        model.addAttribute("user", user);
        return "user-edit";
    }

    @PostMapping("/admin/user/edit")
    public String saveUser(@Valid LibraryUser user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user-edit";
        }

        libraryUserService.saveUser(user);

        return "redirect:/admin";
    }
}
