package com.cavanosa.prueba_rag_gemini.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.document.Document;

import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class DataLoader implements ApplicationRunner {

    @Value("classpath:documents/")
    private Resource documentsFolder;

    private final VectorStore vectorStore;
    private final List<Document> documentRegistry;
    public final ObjectMapper objectMapper;
    public final JdbcTemplate jdbcTemplate;


    public DataLoader(VectorStore vectorStore, List<Document> documentRegistry, ObjectMapper objectMapper, JdbcTemplate jdbcTemplate) {
        this.vectorStore = vectorStore;
        this.documentRegistry = documentRegistry;
        this.objectMapper = objectMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM vector_store", Integer.class
        );
        if(count != null && count > 0) {
            System.out.println("✅ VectorStore ya tiene " + count + " documentos, omitiendo carga.");
            List<Document> existing = jdbcTemplate.query(
                    "SELECT id, content, metadata FROM vector_store",
                    (rs, rowNum) -> {
                        try {
                            Map<String, Object> metadata = objectMapper.readValue(
                                    rs.getString("metadata"),
                                    new TypeReference<Map<String, Object>>() {
                                    }
                            );
                            Document doc = Document.builder()
                                    .id(rs.getString("id"))
                                    .text(rs.getString("content"))
                                    .build();
                            doc.getMetadata().putAll(metadata);
                            return doc;
                        } catch (Exception e) {
                            throw new RuntimeException("Error leyendo metadata", e);
                        }
                    }
            );
            documentRegistry.addAll(existing);
            return;
        }
        File folder = documentsFolder.getFile();
        File[] textFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if(textFiles == null || textFiles.length == 0) {
            System.out.println("no hay documentos");
            return;
        }
        List<Document> docs = new ArrayList<>();
        for(File file: textFiles) {
            String text = Files.readString(file.toPath());
            String jsonFileName = file.getName().replace(".txt", ".json");
            File jsonFile = new File(folder, jsonFileName);

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("fuente_fichero", file.getName());
            metadata.put("fecha", LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            if(jsonFile.exists()) {
                Map<String, Object> jsonMetadata = objectMapper.readValue(jsonFile, Map.class);
                metadata.putAll(jsonMetadata);
            }
            Document doc = Document.builder()
                    .text(text)
                    .metadata(metadata)
                    .build();
            docs.add(doc);

        }
        vectorStore.add(docs);
        documentRegistry.addAll(docs);
        System.out.println("✅ VectorStore ha cargado los documentos.");
    }
}
