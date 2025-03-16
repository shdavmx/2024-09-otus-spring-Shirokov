package ru.otus.hw.security.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.security.models.LibraryRole;
import ru.otus.hw.security.repositories.LibraryRoleRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LibraryRoleServiceImpl implements LibraryRoleService {
    private LibraryRoleRepository libraryRoleRepository;

    @Override
    public List<LibraryRole> findAll() {
        return libraryRoleRepository.findAll();
    }

    @Override
    public LibraryRole findByName(String name) {
        LibraryRole role = libraryRoleRepository.findByName(name);
        if (role == null) {
            throw new EntityNotFoundException("Role with name '%s' not found".formatted(name));
        }
        return role;
    }
}
