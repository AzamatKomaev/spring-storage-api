package ru.azamatkomaev.storage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping(path = "/api/v1/files")
public class FileController {

    @GetMapping
    public ResponseEntity<String> getAllFiles() {
        return ResponseEntity.ok("Get all files");
    }

    @PostMapping
    public ResponseEntity<String> saveFile(
        Principal principal,
        @RequestParam("file") MultipartFile file
    ) {
        System.out.println("Current user: " + principal.getName());
        System.out.println(file.getOriginalFilename());
        return ResponseEntity.ok("It works!");
    }
}
