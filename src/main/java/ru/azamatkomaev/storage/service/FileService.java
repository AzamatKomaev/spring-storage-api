package ru.azamatkomaev.storage.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.azamatkomaev.storage.model.File;
import ru.azamatkomaev.storage.repository.FileRepository;

import java.util.List;

public class FileService {
    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<File> getAll() {
        return fileRepository.findAll();
    }

    public File save() {
        return null;
    }
}
