package com.example.chat.message.repos;

import com.example.chat.message.Message;
import com.google.android.gms.tasks.Task;

import java.util.List;

public interface MessageRepos {
    void sendMessage(Message message);

    Task<List<Message>> getListMessageFromConversationId(String conversationId);
}
