package com.cavanosa.prueba_rag_gemini.model;

import java.util.Map;

public record DocumentMetadata(
        String categoria,
        String fuente,
        String fecha,
        String fuente_fichero
) {
    public Map<String, Object> toMap() {
        return Map.of(
                "categoria", categoria,
                "fuente", fuente,
                "fecha", fecha,
                "fuente_fichero", fuente_fichero
        );
    }
}
