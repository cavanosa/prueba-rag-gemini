package com.cavanosa.prueba_rag_gemini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PruebaRagGeminiApplication {

	public static void main(String[] args) {
		// System.out.println(">>> API KEY: " + System.getenv("GEMINI_API_KEY"));
		SpringApplication.run(PruebaRagGeminiApplication.class, args);
	}

}
