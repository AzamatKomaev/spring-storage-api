package ru.azamatkomaev.storage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.azamatkomaev.storage.model.Event;
import ru.azamatkomaev.storage.model.File;
import ru.azamatkomaev.storage.response.EventResponse;
import ru.azamatkomaev.storage.service.FileService;
import ru.azamatkomaev.storage.service.storage.StorageService;

import java.security.Principal;

@RestController
@RequestMapping(path = "/api/v1/files")
public class FileController {
    private final StorageService storageService;
    private final FileService fileService;

    @Autowired
    public FileController(StorageService storageService, FileService fileService) {
        this.storageService = storageService;
        this.fileService = fileService;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Resource getFileById(@PathVariable("id") Long id) {
        File file = fileService.getById(id);
        return storageService.loadAsResource(file.getFilePath());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse saveFile(
        @RequestParam("file") MultipartFile file,
        Principal principal
    ) {
        String uploadedFilename = storageService.store(file);
        String username = principal.getName();

        Event createdEvent = fileService.save(username, uploadedFilename);
        return EventResponse.toEventResponse(createdEvent);
    }
}
