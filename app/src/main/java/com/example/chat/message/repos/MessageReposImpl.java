package com.example.chat.message.repos;

import com.example.chat.Utils;
import com.example.chat.message.Message;
import com.google.firebase.firestore.EventListener;
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
    public void listenMessages(String conversationId, EventListener<QuerySnapshot> eventListener) {
        firebaseFirestore.collection(Utils.KEY_COLLECTION_CHAT)
                .whereEqualTo(Utils.KEY_CONVERSATION_ID, "2")
                .addSnapshotListener(eventListener);
    }
}
