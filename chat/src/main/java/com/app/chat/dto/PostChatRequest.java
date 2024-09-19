package com.app.chat.dto;

import lombok.Data;

@Data
public class PostChatRequest {
    private String sender;
    private String receiver;
    private String message;
}