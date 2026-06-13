package com.cavanosa.prueba_rag_gemini.repository;

import com.cavanosa.prueba_rag_gemini.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID>, JpaSpecificationExecutor<DocumentEntity> {

    @Query(value = "SELECT id FROM vector_store WHERE metadata ->> 'parent_document_id' = :documentId",nativeQuery = true)
    List<UUID> findChunksIdByDocumentId(@Param("documentId") String documentId);
    @Query("SELECT DISTINCT d.categoria FROM DocumentEntity d ORDER BY d.categoria")
    List<String> findDistinctCategorias();
}
