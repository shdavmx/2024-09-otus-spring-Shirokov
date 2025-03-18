package ru.otus.hw.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.security.models.LibraryRole;

public interface LibraryRoleRepository extends JpaRepository<LibraryRole, Long> {
    LibraryRole findByName(String name);
}
