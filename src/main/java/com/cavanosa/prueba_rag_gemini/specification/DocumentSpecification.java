package com.cavanosa.prueba_rag_gemini.specification;

import com.cavanosa.prueba_rag_gemini.dto.DocumentFilterRequest;
import com.cavanosa.prueba_rag_gemini.entity.DocumentEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DocumentSpecification {

    private DocumentSpecification() {}

    public static Specification<DocumentEntity> withFilters(DocumentFilterRequest filters) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filters.categoria() != null && !filters.categoria().isBlank())
                predicates.add(criteriaBuilder.equal(root.get("categoria"), filters.categoria()));
            if (filters.fuente() != null && !filters.fuente().isBlank())
                predicates.add(criteriaBuilder.equal(root.get("fuente"), filters.fuente()));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
