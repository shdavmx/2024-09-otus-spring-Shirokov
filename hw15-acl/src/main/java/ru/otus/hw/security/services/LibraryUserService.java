package ru.otus.hw.security.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.otus.hw.security.models.LibraryUser;

import java.util.List;

public interface LibraryUserService extends UserDetailsService {
    List<LibraryUser> findAll();

    boolean saveUser(LibraryUser user);

    LibraryUser findUserById(Long id);

    void deleteUserById(Long id);
}
