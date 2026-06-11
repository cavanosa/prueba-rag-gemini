package com.cavanosa.prueba_rag_gemini.utils;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TextExtractor {

    public TextExtractor() {}

    public static String extractText(MultipartFile file) throws IOException {
        if(file.isEmpty())
            throw new IllegalArgumentException("El archivo está vacío");
        if(file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty())
            throw new IllegalArgumentException("archivo sin nombre");
        String fileName = file.getOriginalFilename().toLowerCase();
        if(fileName.endsWith(".txt"))
            return new String(file.getBytes(), StandardCharsets.UTF_8);
        else if (fileName.endsWith(".pdf")) {
            try (PDDocument pdf =
                         Loader.loadPDF(file.getBytes())) {
                PDFTextStripper stripper =
                        new PDFTextStripper();

                return stripper.getText(pdf);
            }
        } else {
            throw new IllegalArgumentException("Formato no soportado");
        }
    }
}
