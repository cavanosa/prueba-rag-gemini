package com.cavanosa.prueba_rag_gemini.dto;

import org.springframework.ai.vectorstore.SearchRequest;

public record ChatRequest (
        String question,
        String categoria
) {
}
