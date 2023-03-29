package ru.azamatkomaev.storage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.azamatkomaev.storage.model.Event;
import ru.azamatkomaev.storage.response.EventResponse;
import ru.azamatkomaev.storage.service.FileService;
import ru.azamatkomaev.storage.service.storage.StorageService;

import java.security.Principal;

@RestController
@RequestMapping(path = "/api/v1/files")
public class FileController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private FileService fileService;

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
