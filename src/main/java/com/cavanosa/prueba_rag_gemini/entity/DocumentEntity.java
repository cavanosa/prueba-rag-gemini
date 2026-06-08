package com.cavanosa.prueba_rag_gemini.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "vector_store")
public class DocumentEntity {

    @Id
    private UUID id;

    @Column(name = "content")
    private String content;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    public UUID getId() {
        return id;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public String getContent() {
        return content;
    }
}
