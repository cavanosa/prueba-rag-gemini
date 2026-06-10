package com.cavanosa.prueba_rag_gemini.service;

import com.cavanosa.prueba_rag_gemini.dto.DocumentDeleteResponse;
import com.cavanosa.prueba_rag_gemini.dto.DocumentDetailResponse;
import com.cavanosa.prueba_rag_gemini.dto.DocumentSummaryResponse;
import com.cavanosa.prueba_rag_gemini.dto.UploadResponse;
import com.cavanosa.prueba_rag_gemini.entity.DocumentEntity;
import com.cavanosa.prueba_rag_gemini.repository.DocumentRepository;
import com.cavanosa.prueba_rag_gemini.utils.DocumentFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
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

    public List<DocumentSummaryResponse> getall() {
        return documentRepository.findAll().stream().map(DocumentSummaryResponse::fromEntity).toList();
    }

    public DocumentDetailResponse findById(UUID id) {
      DocumentEntity doc = documentRepository.findById(id)
              .orElseThrow(()-> new NoSuchElementException("no existe el documento con id " + id +"."));
      return DocumentDetailResponse.from(doc);
    }

    public UploadResponse upload(
            MultipartFile file,
            String categoria,
            String fuente
    ) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío.");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".txt")) {
            throw new IllegalArgumentException("Solo se permiten archivos .txt.");
        }

        try {
            String text = new String(file.getBytes());
            UUID documentId = UUID.randomUUID();
            Document doc = documentFactory.build(
                    text,
                    categoria,
                    fuente,
                    file.getOriginalFilename()
            );
            List<Document> chunks =
                    tokenTextSplitter.split(List.of(doc));

            DocumentEntity documentEntity = new DocumentEntity(
                    documentId,
                    fileName,
                    categoria,
                    fuente,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd--MM-yyyy")),
                    chunks.size()
            );
            documentRepository.save(documentEntity);

            vectorStore.add(chunks);
            System.out.println("Document ID = " + documentEntity.getId());
            return new UploadResponse(
                    fileName,
                    chunks.size(),
                    file.getSize(),
                    categoria,
                    fuente
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    e.getLocalizedMessage()
            );
        }
    }

    public DocumentDeleteResponse delete(UUID documentId) {
        DocumentEntity doc = documentRepository.findById(documentId)
                .orElseThrow(()-> new NoSuchElementException("no existe el documento con id " +documentId +"."));
        List<String> chunkIds = documentRepository.findChunksIdByDocumentId(documentId.toString())
                .stream()
                .map(UUID::toString)
                .toList();
        if(!chunkIds.isEmpty())
            vectorStore.delete(chunkIds);
        documentRepository.delete(doc);
        return new DocumentDeleteResponse(doc.getId(), doc.getFileName(), chunkIds.size());
    }


}
