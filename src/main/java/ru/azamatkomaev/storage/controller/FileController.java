package ru.azamatkomaev.storage.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.azamatkomaev.storage.service.storage.StorageService;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/files")
public class FileController {

    @Autowired
    private StorageService storageService;

    @GetMapping
    public ResponseEntity<String> getAllFiles() {
        return ResponseEntity.ok("Get all files");
    }

    @PostMapping
    public ResponseEntity<String> saveFile(
        @RequestParam("file") MultipartFile file,
        Principal principal
    ) {
        String uploadedFilename = storageService.store(file);
        String username = principal.getName();

        return ResponseEntity.ok(file.getOriginalFilename());
    }
}
