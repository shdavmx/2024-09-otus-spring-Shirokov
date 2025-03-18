package ru.otus.hw.security.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LibraryUserFormModel {
    private Long id;

    private String username;

    private String password;

    private String email;

    private List<Long> authorityIds;
}
