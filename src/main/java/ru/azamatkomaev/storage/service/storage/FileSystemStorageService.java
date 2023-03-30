package ru.azamatkomaev.storage.service.storage;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.azamatkomaev.storage.exception.FileUploadServerException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Service
public class FileSystemStorageService implements StorageService {
    private final ClassPathResource pathResource = new ClassPathResource("media");

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
    public Resource loadAsResource(String filename) {
        try {
            File file = new File(pathResource.getFile().getPath() + "/" + filename);
            return new FileSystemResource(file);
        } catch (IOException e) {
            throw new FileUploadServerException(e.getMessage());
        }
    }

    @Override
    public void deleteAllFiles() {
        try {
            Arrays.stream(Objects.requireNonNull(pathResource.getFile().listFiles())).forEach(File::delete);
        } catch (IOException e) {
            throw new FileUploadServerException(e.getMessage());
        }
    }
}
