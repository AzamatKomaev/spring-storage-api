package ru.azamatkomaev.storage.service;

import org.springframework.stereotype.Service;
import ru.azamatkomaev.storage.exception.NotFoundException;
import ru.azamatkomaev.storage.model.Event;
import ru.azamatkomaev.storage.model.File;
import ru.azamatkomaev.storage.model.User;
import ru.azamatkomaev.storage.repository.FileRepository;

import java.util.List;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final EventService eventService;
    private final UserService userService;

    public FileService(FileRepository fileRepository, EventService eventService, UserService userService) {
        this.fileRepository = fileRepository;
        this.eventService = eventService;
        this.userService = userService;
    }

    public List<File> getAll() {
        return fileRepository.findAll();
    }

    public File getById(Long id) {
        return fileRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Cannot find any file with id: " + id));
    }

    public Event save(String username, String filename) {
        User user = userService.getByUsername(username);
        File uploadedFile = fileRepository.save(
            File.builder().name(filename).filePath(filename).build()
        );
        return eventService.save(user, uploadedFile);
    }
}
