package ru.azamatkomaev.storage.service.storage;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.azamatkomaev.storage.exception.FileUploadServerException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class FileSystemStorageService implements StorageService {
    private final ClassPathResource pathResource = new ClassPathResource("media", this.getClass());

    @Override
    public String store(MultipartFile file) {
        try {
            File outputFile = new File(pathResource.getFile().getPath() + "/" + file.getOriginalFilename());
            file.transferTo(outputFile);
            return file.getOriginalFilename();
        } catch (IOException e) {
            throw new FileUploadServerException(e.getMessage());
        }
    }

    @Override
    public Path load(String filename) {
        return null;
    }

    @Override
    public void deleteByFilename(String filename) {

    }

    @Override
    public void deleteAll() {

    }
}
