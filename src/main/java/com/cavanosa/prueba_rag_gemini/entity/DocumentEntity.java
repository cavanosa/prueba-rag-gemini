package com.cavanosa.prueba_rag_gemini.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "documents")
public class DocumentEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String fileName;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String mimeType;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private String fuente;

    @Column(name = "upload_date", nullable = false)
    private String uploadDate;

    @Column(name = "chunks_count", nullable = false)
    private Integer chunksCount;

    public DocumentEntity(){}

    public DocumentEntity(UUID id, String fileName, String content, Long fileSize, String mimeType, String categoria, String fuente, String uploadDate, Integer chunksCount) {
        this.id = id;
        this.fileName = fileName;
        this.content = content;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.categoria = categoria;
        this.fuente = fuente;
        this.uploadDate = uploadDate;
        this.chunksCount = chunksCount;
    }

    public UUID getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContent() {
        return content;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getFuente() {
        return fuente;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public Integer getChunksCount() {
        return chunksCount;
    }
}
