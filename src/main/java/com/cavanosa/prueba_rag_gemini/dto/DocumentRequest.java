package com.cavanosa.prueba_rag_gemini.dto;

public record DocumentRequest(
        String text,
        String categoria,
        String fuente,
        String fuente_fichero
) {
}
