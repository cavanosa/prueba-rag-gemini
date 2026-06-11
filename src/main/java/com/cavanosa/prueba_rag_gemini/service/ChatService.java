package com.cavanosa.prueba_rag_gemini.service;


import com.cavanosa.prueba_rag_gemini.dto.ChatRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;


@Service
public class ChatService {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public ChatService(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    public String ask(ChatRequest request) {
        SearchRequest.Builder searchRequestBuilder = SearchRequest.builder()
                .query(request.question());
        if (request.categoria() != null && !request.categoria().isBlank()) {
            searchRequestBuilder.filterExpression("categoria == '" + request.categoria() + "'");
        }
        SearchRequest searchRequest = searchRequestBuilder.build();

        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(searchRequest)
                .build();
        return chatClient.prompt()
                .user(request.question())
                .advisors(advisor)
                .call()
                .content();
    }
}
