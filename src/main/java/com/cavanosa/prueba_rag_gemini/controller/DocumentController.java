package com.cavanosa.prueba_rag_gemini.controller;

import com.cavanosa.prueba_rag_gemini.dto.DocumentRequest;
import com.cavanosa.prueba_rag_gemini.model.ApiResponse;
import com.cavanosa.prueba_rag_gemini.dto.DocumentResponse;
import com.cavanosa.prueba_rag_gemini.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DocumentResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(documentService.getAll()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DocumentResponse>> add(@RequestBody DocumentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(documentService.add(request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        documentService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.noContent());
    }
}
