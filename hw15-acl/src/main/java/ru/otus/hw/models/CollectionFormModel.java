package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CollectionFormModel {
    private Long id;

    private String name;

    private String description;

    private List<Long> bookIds;
}
