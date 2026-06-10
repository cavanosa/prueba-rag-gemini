package com.cavanosa.prueba_rag_gemini.dto;

import java.util.UUID;

public record DocumentDeleteResponse(
        UUID documentId,
        String message,
        int deletedChunks
) {
}
