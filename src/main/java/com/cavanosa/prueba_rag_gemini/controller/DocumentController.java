package com.cavanosa.prueba_rag_gemini.controller;

import com.cavanosa.prueba_rag_gemini.dto.UploadResponse;
import com.cavanosa.prueba_rag_gemini.model.ApiResponse;
import com.cavanosa.prueba_rag_gemini.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UploadResponse>> upload(
            @RequestParam("file")MultipartFile file,
            @RequestParam String categoria,
            @RequestParam String fuente
            ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(documentService.upload(file, categoria, fuente)));
    }

}
