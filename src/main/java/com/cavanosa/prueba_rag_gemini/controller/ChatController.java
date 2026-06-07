package com.cavanosa.prueba_rag_gemini.controller;


import com.cavanosa.prueba_rag_gemini.model.ApiResponse;
import com.cavanosa.prueba_rag_gemini.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> chat(@RequestBody String question) {
        return ResponseEntity.ok(ApiResponse.ok(chatService.ask(question)));
    }
}
