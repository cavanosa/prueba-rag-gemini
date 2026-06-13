package com.cavanosa.prueba_rag_gemini.service;

import com.cavanosa.prueba_rag_gemini.dto.*;
import com.cavanosa.prueba_rag_gemini.entity.DocumentEntity;
import com.cavanosa.prueba_rag_gemini.repository.DocumentRepository;
import com.cavanosa.prueba_rag_gemini.specification.DocumentSpecification;
import com.cavanosa.prueba_rag_gemini.utils.DocumentFactory;
import com.cavanosa.prueba_rag_gemini.utils.TextExtractor;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class DocumentService {

    private final VectorStore vectorStore;
    private final TokenTextSplitter tokenTextSplitter;
    private final DocumentFactory documentFactory;
    private final DocumentRepository documentRepository;

    public DocumentService(VectorStore vectorStore, TokenTextSplitter tokenTextSplitter, DocumentFactory documentFactory, DocumentRepository documentRepository) {
        this.vectorStore = vectorStore;
        this.tokenTextSplitter = tokenTextSplitter;
        this.documentFactory = documentFactory;
        this.documentRepository = documentRepository;
    }

    public List<String> getCategorias() {
        return documentRepository.findDistinctCategorias();
    }

    //specification
    public List<DocumentSummaryResponse> findBySpecification(DocumentFilterRequest filters) {
        Specification<DocumentEntity> spec = DocumentSpecification.withFilters(filters);
        return documentRepository.findAll(spec).stream()
                .map(DocumentSummaryResponse::from).toList();
    }

    public DocumentDetailResponse findById(UUID id) {
        DocumentEntity doc = documentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("no existe el documento con id " + id + "."));
        return DocumentDetailResponse.from(doc);
    }

    public UploadResponse upload(
            MultipartFile file,
            String categoria,
            String fuente
    ) {

        String fileName = file.getOriginalFilename();
        String text;
        try {
            text = TextExtractor.extractText(file);
        } catch (IOException e) {
            throw new IllegalArgumentException("No se pudo procesar el archivo");
        }
        UUID documentId = UUID.randomUUID();
        Document doc = documentFactory.build(
                documentId,
                text,
                categoria,
                fuente,
                file.getOriginalFilename()
        );

        List<Document> chunks =
                tokenTextSplitter.split(List.of(doc));
        Long fileSize = file.getSize();
        String mimeType = file.getContentType();
        DocumentEntity documentEntity = new DocumentEntity(
                documentId,
                fileName,
                text,
                fileSize,
                mimeType,
                categoria,
                fuente,
                doc.getMetadata().get("fecha").toString(),
                chunks.size()
        );
        vectorStore.add(chunks);
        documentRepository.save(documentEntity);


        return new UploadResponse(
                fileName,
                chunks.size(),
                file.getSize(),
                categoria,
                fuente
        );

    }

    public DocumentDeleteResponse delete(UUID documentId) {
        DocumentEntity doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new NoSuchElementException("no existe el documento con id " + documentId + "."));
        System.out.println("DELETE DOCUMENT ID = " + documentId);
        List<String> chunkIds = documentRepository.findChunksIdByDocumentId(documentId.toString())
                .stream()
                .map(UUID::toString)
                .toList();
        System.out.println("Chunks encontrados = " + chunkIds.size());
        chunkIds.forEach(c -> System.out.println(c));
        if (!chunkIds.isEmpty()) {
            vectorStore.delete(chunkIds);
            System.out.println("Delete ejecutado");
        }
        documentRepository.delete(doc);
        return new DocumentDeleteResponse(doc.getId(), doc.getFileName(), chunkIds.size());
    }


}
