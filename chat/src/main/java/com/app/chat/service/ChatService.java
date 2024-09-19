package com.app.chat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.chat.dto.PostChatRequest;
import com.app.chat.model.Chat;
import com.app.chat.repository.ChatRepository;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    public List<Chat> getChat() {
        return chatRepository.findAll();
    }

    public String insertChat(PostChatRequest postChatRequest) {
        chatRepository.insert(Chat.builder()
            .sender(postChatRequest.getSender())
            .receiver(postChatRequest.getReceiver())
            .message(postChatRequest.getMessage())
            .build());
        return "success";
    }
}