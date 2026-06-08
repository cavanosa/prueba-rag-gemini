package com.cavanosa.prueba_rag_gemini.dto;

import com.cavanosa.prueba_rag_gemini.entity.DocumentEntity;
import org.springframework.ai.document.Document;

import java.util.Map;

public record DocumentResponse(
        String id,
        String text,
        Map<String, Object> metadata
) {
    public static DocumentResponse from(Document doc) {
        return new DocumentResponse(
                doc.getId(),
                doc.getText(),
                doc.getMetadata()
        );
    }

    public static DocumentResponse from(DocumentEntity entity) {
        return new DocumentResponse(
                entity.getId().toString(),
                entity.getContent(),
                entity.getMetadata()
        );
    }
}
