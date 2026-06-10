package com.cavanosa.prueba_rag_gemini.controller;

import com.cavanosa.prueba_rag_gemini.dto.DocumentDetailResponse;
import com.cavanosa.prueba_rag_gemini.dto.DocumentSummaryResponse;
import com.cavanosa.prueba_rag_gemini.dto.UploadResponse;
import com.cavanosa.prueba_rag_gemini.model.ApiResponse;
import com.cavanosa.prueba_rag_gemini.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DocumentSummaryResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(documentService.getall()));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentDetailResponse>> findById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(documentService.findById(id)));
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
