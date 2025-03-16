package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Collection;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
}
