package com.example.chat;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String KEY_NAME = "name";
    public static final String KEY_TYPE = "type";
    public static final String KEY_IS_DELETED = "isDeleted";
    public static final String KEY_CONVERSATION_ID = "conversationId";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_IS_VISIBILITY = "visibility";
    public static final String KEY_SENDING_TIME = "sendingTime";
    public static final String KEY_CONVERSATION_NAME = "conversationName";
    public static final String KEY_CONVERSATION_IMAGE = "conversationImage";

    public static <T> T convertFromSnapshot(@Nullable DocumentSnapshot documentSnapshot, Class<T> type) {
        if (documentSnapshot == null) {
            return null;
        }
        return documentSnapshot.toObject(type);
    }

    public static <T> List<T> convertFromSnapshot(@Nullable QuerySnapshot documents, Class<T> type) {
        if (documents == null) {
            return new ArrayList<>();
        }

        List<T> list = new ArrayList<>();
        for (DocumentSnapshot document : documents) {
            T object = document.toObject(type);
            if (object != null) {
                list.add(object);
            }
        }
        return list;
    }
}
