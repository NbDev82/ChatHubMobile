package com.example.chat.message.repos;

import com.example.chat.message.MessageNotFoundException;
import com.example.chat.Utils;
import com.example.chat.message.Message;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MessageReposImpl implements MessageRepos{
    private static final String TAG = MessageReposImpl.class.getSimpleName();

    private final FirebaseFirestore firebaseFirestore;

    public MessageReposImpl() {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void sendMessage(Message message) {
        firebaseFirestore.collection(Utils.KEY_COLLECTION_CHAT).add(message.convertHashMap());
    }

    @Override
    public Task<List<Message>> getListMessageFromConversationId(String conversationId) {
        return firebaseFirestore.collection(Utils.KEY_COLLECTION_CHAT)
                .whereEqualTo(Utils.KEY_CONVERSATION_ID, conversationId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot documents = task.getResult();
                        List<Message> messages = Utils.convertFromSnapshot(documents, Message.class);
                        if (messages == null) {
                            throw new MessageNotFoundException("Could not find any chat with conversationId=" + conversationId);
                        }
                        return messages;
                    } else {
                        throw task.getException();
                    }
                });
    }
}
