package com.cavanosa.prueba_rag_gemini.dto;

import java.util.UUID;

public record DeleteDocumentResponse(
        UUID documentId,
        String message
) {
}
