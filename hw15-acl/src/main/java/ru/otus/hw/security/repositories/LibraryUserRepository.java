package ru.otus.hw.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.security.models.LibraryUser;

public interface LibraryUserRepository extends JpaRepository<LibraryUser, String> {
    LibraryUser findByUsername(String username);
}
