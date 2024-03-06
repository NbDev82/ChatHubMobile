package com.example.chat.conversation.repos;

import com.example.chat.conversation.Conversation;
import com.google.android.gms.tasks.Task;

public interface ConversationRepos {
    Task<Conversation> getConversationById(String conversationId);
}
