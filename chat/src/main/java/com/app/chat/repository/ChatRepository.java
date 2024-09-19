package com.app.chat.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.chat.model.Chat;

public interface ChatRepository extends MongoRepository<Chat, String> {
}