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
    public static final String KEY_IMAGE = "image";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_USER = "user";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_RECEIVER_IMAGE = "receiverImage";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_IS_VISIBILITY = "visibility";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";
    public static final String KEY_SENDING_TIME = "sendingTime";

    public static final <T> T convertFromSnapshot(@Nullable DocumentSnapshot documentSnapshot, Class<T> type) {
        if (documentSnapshot == null) {
            return null;
        }
        return documentSnapshot.toObject(type);
    }

    public static final <T> List<T> convertFromSnapshot(@Nullable QuerySnapshot documents, Class<T> type) {
        if (documents == null) {
            // Handle the case where documents is null (return empty list or throw an exception)
            return new ArrayList<>(); // Or throw a custom exception
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
