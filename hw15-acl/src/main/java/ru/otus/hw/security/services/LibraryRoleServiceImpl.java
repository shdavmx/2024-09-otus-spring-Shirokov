package ru.otus.hw.security.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.security.models.AuthorityNames;
import ru.otus.hw.security.models.LibraryRole;
import ru.otus.hw.security.repositories.LibraryRoleRepository;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class LibraryRoleServiceImpl implements LibraryRoleService {
    private static final AuthorityNames DEFAULT_AUTHORITY = AuthorityNames.ROLE_USER;

    private final LibraryRoleRepository libraryRoleRepository;

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

    @Override
    public LibraryRole getDefaultUserRole() {
        return findByName(DEFAULT_AUTHORITY.name());
    }

    @Override
    public List<LibraryRole> findAllById(Set<Long> ids) {
        return libraryRoleRepository.findAllById(ids);
    }
}
