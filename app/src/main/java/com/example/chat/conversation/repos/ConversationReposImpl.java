package com.example.chat.conversation.repos;

import com.google.firebase.firestore.FirebaseFirestore;


public class ConversationReposImpl implements ConversationRepos{
    private static final String TAG = ConversationReposImpl.class.getSimpleName();

    private final FirebaseFirestore firebaseFirestore;

    public ConversationReposImpl() {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }
}
