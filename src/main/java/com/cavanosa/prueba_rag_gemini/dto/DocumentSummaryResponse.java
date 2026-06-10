package com.cavanosa.prueba_rag_gemini.dto;

import com.cavanosa.prueba_rag_gemini.entity.DocumentEntity;

import java.util.UUID;

public record DocumentSummaryResponse(
        UUID id,
        String filename,
        String categoria,
        String fuente,
        String uploadDate,
        Integer chunksCount
) {

    public static DocumentSummaryResponse fromEntity(DocumentEntity entity) {
        return new DocumentSummaryResponse(
                entity.getId(),
                entity.getFileName(),
                entity.getCategoria(),
                entity.getFuente(),
                entity.getUploadDate(),
                entity.getChunksCount()
        );
    }
}
