package ru.azamatkomaev.storage.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.azamatkomaev.storage.model.Event;

public interface EventRepository extends ListCrudRepository<Event, Long> {
}
