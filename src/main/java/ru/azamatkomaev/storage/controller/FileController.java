package ru.azamatkomaev.storage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/files")
public class FileController {

    @GetMapping
    public ResponseEntity<String> getAllFiles() {
        return ResponseEntity.ok("Get all files");
    }
}
