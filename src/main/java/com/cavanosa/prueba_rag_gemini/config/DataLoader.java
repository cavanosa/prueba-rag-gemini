package com.cavanosa.prueba_rag_gemini.config;

import com.cavanosa.prueba_rag_gemini.repository.DocumentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.document.Document;

import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
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
    private final DocumentRepository documentRepository;
    public final ObjectMapper objectMapper;


    public DataLoader(VectorStore vectorStore, DocumentRepository documentRepository, ObjectMapper objectMapper) {
        this.vectorStore = vectorStore;
        this.documentRepository = documentRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(documentRepository.count() > 0) {
            System.out.println("✅ VectorStore ya tiene " + documentRepository.count() + " documentos, omitiendo carga.");
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
        System.out.println("✅ VectorStore ha cargado los documentos.");
    }
}
