package ru.otus.hw.security.services;

import ru.otus.hw.security.models.LibraryRole;

import java.util.List;

public interface LibraryRoleService {
    List<LibraryRole> findAll();

    LibraryRole findByName(String name);
}
