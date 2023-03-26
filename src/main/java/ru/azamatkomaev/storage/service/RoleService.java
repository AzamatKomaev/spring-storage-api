package ru.azamatkomaev.storage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.azamatkomaev.storage.exception.NotFoundException;
import ru.azamatkomaev.storage.model.Role;
import ru.azamatkomaev.storage.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    public Role getById(Long id) {
        return roleRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Cannot find any role with id:" + id));
    }

    public Role getByName(String name) {
        return roleRepository
            .findByName(name)
            .orElseThrow(() -> new NotFoundException("Cannot find any role with name:" + name));
    }
}
