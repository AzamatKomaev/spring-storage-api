package ru.azamatkomaev.storage.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.azamatkomaev.storage.model.User;

import java.util.Optional;

public interface UserRepository extends ListCrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
