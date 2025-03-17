package ru.otus.hw.security.services;

import ru.otus.hw.security.models.LibraryRole;

import java.util.List;
import java.util.Set;

public interface LibraryRoleService {
    List<LibraryRole> findAll();

    LibraryRole findByName(String name);

    LibraryRole getDefaultUserRole();

    List<LibraryRole> findAllById(Set<Long> ids);
}
