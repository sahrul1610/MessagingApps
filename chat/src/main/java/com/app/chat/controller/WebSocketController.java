package com.app.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.app.chat.dto.PostChatRequest;

@Controller
public class WebSocketController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public PostChatRequest send(PostChatRequest chatMessage) {
        return chatMessage;
    }
}