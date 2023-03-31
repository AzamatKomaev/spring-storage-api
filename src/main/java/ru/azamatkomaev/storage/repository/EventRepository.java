package ru.azamatkomaev.storage.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.azamatkomaev.storage.model.Event;

import java.util.Optional;

public interface EventRepository extends ListCrudRepository<Event, Long> {
    Optional<Event> findByFileId(Long fileId);
}
