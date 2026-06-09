package com.cavanosa.prueba_rag_gemini.service;

import com.cavanosa.prueba_rag_gemini.dto.UploadResponse;
import com.cavanosa.prueba_rag_gemini.utils.DocumentFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentService {

    private final VectorStore vectorStore;
    private final TokenTextSplitter tokenTextSplitter;
    private final DocumentFactory documentFactory;

    public DocumentService(VectorStore vectorStore, TokenTextSplitter tokenTextSplitter, DocumentFactory documentFactory) {
        this.vectorStore = vectorStore;
        this.tokenTextSplitter = tokenTextSplitter;
        this.documentFactory = documentFactory;
    }

    public UploadResponse upload(
            MultipartFile file,
            String categoria,
            String fuente
    ) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío.");
        }

        if (!file.getOriginalFilename().endsWith(".txt")) {
            throw new IllegalArgumentException("Solo se permiten archivos .txt.");
        }

        try {
            String text = new String(file.getBytes());

            Document doc = documentFactory.build(
                    text,
                    categoria,
                    fuente,
                    file.getOriginalFilename()
            );

            List<Document> chunks =
                    tokenTextSplitter.split(List.of(doc));

            vectorStore.add(chunks);

            return new UploadResponse(
                    file.getOriginalFilename(),
                    chunks.size(),
                    file.getSize(),
                    categoria,
                    fuente
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error leyendo el archivo",
                    e
            );
        }
    }


}
