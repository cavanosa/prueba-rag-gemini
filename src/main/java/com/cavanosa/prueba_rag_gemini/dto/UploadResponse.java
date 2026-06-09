package com.cavanosa.prueba_rag_gemini.dto;

public record UploadResponse(
        String filename,
        int chunksCreated,
        long fileSize,
        String categoria,
        String fuente
) {
}
