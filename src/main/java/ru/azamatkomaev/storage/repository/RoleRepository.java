package ru.azamatkomaev.storage.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.azamatkomaev.storage.model.Role;

import java.util.Optional;

public interface RoleRepository extends ListCrudRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
