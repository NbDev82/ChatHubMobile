package com.example.chat.conversation.repos;

import com.example.chat.Utils;
import com.example.chat.conversation.Conversation;
import com.example.chat.conversation.ConversationNotFoundException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ConversationReposImpl implements ConversationRepos{
    private static final String TAG = ConversationReposImpl.class.getSimpleName();

    private final FirebaseFirestore firebaseFirestore;

    public ConversationReposImpl() {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public Task<Conversation> getConversationById(String conversationId) {
        return firebaseFirestore.collection(Utils.KEY_COLLECTION_CONVERSATIONS)
                .document(conversationId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        Conversation conversation = Utils.convertFromSnapshot(document, Conversation.class);
                        if (conversation == null) {
                            throw new ConversationNotFoundException("Could not find any conversation with id=" + conversationId);
                        }
                        return conversation;
                    } else {
                        throw task.getException();
                    }
                });
    }
}
