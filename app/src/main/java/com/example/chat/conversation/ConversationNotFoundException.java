package com.example.chat.conversation;

public class ConversationNotFoundException extends RuntimeException{
    public ConversationNotFoundException(String message) {
        super(message);
    }
}
