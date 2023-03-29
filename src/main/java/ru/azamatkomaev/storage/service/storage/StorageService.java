package ru.azamatkomaev.storage.service.storage;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {
    String store(MultipartFile file);
    Path load(String filename);
    void deleteByFilename(String filename);
    void deleteAll();
}
