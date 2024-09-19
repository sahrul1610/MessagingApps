package com.app.chat.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.app.chat.dto.PostChatRequest;
import com.app.chat.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class Consumer {

    @Autowired
    private ChatService chatService;

    @KafkaListener(topics = "chat", groupId = "a")
    public void listen(String message) throws JsonMappingException, JsonProcessingException {
        chatService.insertChat(new ObjectMapper().readValue(message, PostChatRequest.class));
    }
}