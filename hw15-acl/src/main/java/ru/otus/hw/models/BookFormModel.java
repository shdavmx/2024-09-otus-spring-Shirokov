package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class BookFormModel {
    private long id;

    private String title;

    private long authorId;

    private List<Long> genreIds;
}
