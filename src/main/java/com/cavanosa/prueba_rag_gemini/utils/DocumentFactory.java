package com.cavanosa.prueba_rag_gemini.utils;

import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class DocumentFactory {

    public Document build(
            UUID documentId,
            String text,
            String categoria,
            String fuente,
            String fuente_fichero) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("categoria", categoria);
        metadata.put("fuente", fuente);
        metadata.put("fuente_fichero", fuente_fichero);
        metadata.put("fecha", LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        metadata.put("parent_document_id", documentId);

        return Document.builder()
                .text(text)
                .metadata(metadata)
                .build();
    }

    public Document build(
            String text,
            Map<String, Object> metadata
    ) {

        return Document.builder()
                .text(text)
                .metadata(metadata)
                .build();
    }
}
