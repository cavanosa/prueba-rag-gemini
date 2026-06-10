package com.cavanosa.prueba_rag_gemini.repository;

import com.cavanosa.prueba_rag_gemini.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID> {
}
