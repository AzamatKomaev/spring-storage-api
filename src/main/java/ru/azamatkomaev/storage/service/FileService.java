package ru.azamatkomaev.storage.service;

import ru.azamatkomaev.storage.model.File;
import ru.azamatkomaev.storage.repository.FileRepository;

import java.util.List;

public class FileService {
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<File> getAll() {
        return fileRepository.findAll();
    }

    public File save(String username, String filename) {
        return null;
    }
}
