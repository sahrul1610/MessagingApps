package com.app.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.chat.dto.PostChatRequest;
import com.app.chat.kafka.Producer;
import com.app.chat.model.Chat;
import com.app.chat.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/v1")
@CrossOrigin
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private Producer producer;

    @GetMapping("/chat")
    public ResponseEntity<List<Chat>> getLocomotifSummary() {
        return ResponseEntity.ok(chatService.getChat());
    }

    @PostMapping("/chat")
    public ResponseEntity<String> postLocomotifSummary(@RequestBody PostChatRequest postChatRequest)
            throws JsonProcessingException {
        producer.sendMessage("chat", new ObjectMapper().writeValueAsString(postChatRequest));
        return ResponseEntity.ok("success");
    }
}