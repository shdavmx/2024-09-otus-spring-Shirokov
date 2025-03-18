package ru.otus.hw.security.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.security.models.AuthorityNames;
import ru.otus.hw.security.models.LibraryRole;
import ru.otus.hw.security.models.LibraryUser;
import ru.otus.hw.security.repositories.LibraryUserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LibraryUserServiceImpl implements LibraryUserService {
    private final LibraryRoleService libraryRoleService;

    private final LibraryUserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LibraryUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User '%s' not found".formatted(username));
        }

        return user;
    }

    @Override
    public List<LibraryUser> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public boolean saveUser(LibraryUser user) {
        LibraryUser existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            return false;
        }

        if (user.getAuthorities() == null || user.getAuthorities().isEmpty()) {
            LibraryRole userRole = libraryRoleService.findByName(AuthorityNames.ROLE_USER.name());
            user.setUserAuthorities(List.of(userRole));
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return true;
    }

    @Override
    public LibraryUser findUserById(Long id) {
        Optional<LibraryUser> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User with id '%d' not found");
        }

        return user.get();
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
