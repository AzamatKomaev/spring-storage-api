package ru.azamatkomaev.storage.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.azamatkomaev.storage.model.File;

import java.util.Optional;

public interface FileRepository extends ListCrudRepository<File, Long> {
}
