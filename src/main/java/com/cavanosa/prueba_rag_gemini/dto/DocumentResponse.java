package com.cavanosa.prueba_rag_gemini.dto;

import com.cavanosa.prueba_rag_gemini.model.DocumentMetadata;
import org.springframework.ai.document.Document;

public record DocumentResponse(
        String id,
        String text,
        DocumentMetadata metadata
) {
    public static DocumentResponse from(Document doc) {
        return new DocumentResponse(
                doc.getId(),
                doc.getText(),
                new DocumentMetadata(
                        (String) doc.getMetadata().get("categoria"),
                        (String) doc.getMetadata().get("fuente"),
                        (String) doc.getMetadata().get("fecha"),
                        (String) doc.getMetadata().get("fuente_fichero")
                )
        );
    }
}
