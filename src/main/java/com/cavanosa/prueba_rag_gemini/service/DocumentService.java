package com.cavanosa.prueba_rag_gemini.service;

import com.cavanosa.prueba_rag_gemini.dto.DocumentRequest;
import com.cavanosa.prueba_rag_gemini.dto.DocumentResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DocumentService {

    private final VectorStore vectorStore;
    private final List<Document> documentRegistry;

    public DocumentService(VectorStore vectorStore, List<Document> documentRegistry) {
        this.vectorStore = vectorStore;
        this.documentRegistry = documentRegistry;
    }

    public List<DocumentResponse> getAll() {
        return documentRegistry.stream()
                .map(DocumentResponse::from).toList();
    }

    public DocumentResponse add(DocumentRequest request ) {
        String fuente_fichero = (request.fuente_fichero() != null) ? request.fuente_fichero() : "api";
        Document doc = Document.builder().text(request.text())
                .metadata("categoria", request.categoria())
                .metadata("fuente", request.fuente())
                .metadata("fuente_fichero", fuente_fichero)
                .metadata("fecha", LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
        .build();
        vectorStore.add(List.of(doc));
        documentRegistry.add(doc);
        return DocumentResponse.from(doc);
    }

    public String delete(String id) {
        boolean exists = documentRegistry.stream()
                        .anyMatch(doc-> doc.getId().equals(id));
        if(!exists)
            throw new NoSuchElementException("no existe ningún documento con id " + id + ".");
        vectorStore.delete(List.of(id));
        documentRegistry.removeIf(doc -> doc.getId().equals(id));
        return "El documento con id " + id + " ha sido eliminado con éxito.";
    }
}
