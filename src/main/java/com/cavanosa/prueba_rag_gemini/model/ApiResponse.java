package com.cavanosa.prueba_rag_gemini.model;

import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
        int status,
        String message,
        T data
) {
    public static <T> ApiResponse<T> ok (T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), "OK", data);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Created", data);
    }

    public static ApiResponse<Void> noContent() {
        return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "No Content", null);
    }
}
