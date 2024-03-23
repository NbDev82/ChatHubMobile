package com.example.chat.message.repos;

import com.example.chat.message.Message;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public interface MessageRepos {
    void sendMessage(Message message);

    void listenMessages(String conversationId, EventListener<QuerySnapshot> eventListener);
}
