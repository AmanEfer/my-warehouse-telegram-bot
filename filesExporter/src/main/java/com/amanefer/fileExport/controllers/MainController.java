package com.amanefer.fileExport.controllers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/filesExport")
public class MainController {

    public static final String FILE_NAME = "textExample.csv";

    @GetMapping
    public byte[] getCsv() {
        try {
            Resource resource = new ClassPathResource(FILE_NAME);

            return Files.readAllBytes(Path.of(resource.getURI()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
