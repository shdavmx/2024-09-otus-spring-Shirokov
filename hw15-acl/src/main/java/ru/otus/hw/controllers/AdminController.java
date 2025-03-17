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
import ru.otus.hw.security.models.LibraryRole;
import ru.otus.hw.security.models.LibraryUser;
import ru.otus.hw.security.models.LibraryUserFormModel;
import ru.otus.hw.security.services.LibraryRoleService;
import ru.otus.hw.security.services.LibraryUserService;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class AdminController {
    private final LibraryRoleService libraryRoleService;

    private final LibraryUserService libraryUserService;

    @GetMapping("/admin")
    public String getUsers(Model model) {
        List<LibraryUser> users = libraryUserService.findAll();
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping("/admin/user/edit/{id}")
    public String getEditUser(@PathVariable("id") Long id, Model model) {
        List<LibraryRole> roles = libraryRoleService.findAll();
        LibraryUserFormModel user = new LibraryUserFormModel();
        if (id == 0) {
            user.setUsername("new_user");
            LibraryRole userRole = libraryRoleService.getDefaultUserRole();
            user.setAuthorityIds(List.of(userRole.getId()));
        } else {
            LibraryUser libUser = libraryUserService.findUserById(id);
            user.setId(libUser.getId());
            user.setUsername(libUser.getUsername());
            user.setEmail(libUser.getEmail());
            user.setAuthorityIds(libUser.getUserAuthorities().stream().map(LibraryRole::getId).toList());
        }

        model.addAttribute("user", user);
        model.addAttribute("authorities", roles);
        return "user-edit";
    }

    @PostMapping("/admin/user/edit")
    public String saveUser(@Valid LibraryUserFormModel user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user-edit";
        }

        LibraryUser libUser = new LibraryUser();
        libUser.setId(user.getId());
        libUser.setEmail(user.getEmail());
        libUser.setUsername(user.getUsername());
        libUser.setPassword(user.getPassword());

        List<LibraryRole> roles = libraryRoleService.findAllById(new HashSet<>(user.getAuthorityIds()));
        libUser.setUserAuthorities(roles);

        libraryUserService.saveUser(libUser);

        return "redirect:/admin";
    }

    @PostMapping("/admin/user/delete")
    public String deleteBookById(@RequestParam("id") Long id) {
        libraryUserService.deleteUserById(id);
        return "redirect:/admin";
    }
}
