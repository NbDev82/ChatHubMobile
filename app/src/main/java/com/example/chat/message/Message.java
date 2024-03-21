package com.example.chat.message;

import android.util.Log;

import com.example.chat.Utils;
import com.example.chat.enums.Evisible;
import com.google.firebase.firestore.DocumentChange;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


public class Message {
    private String senderId, message;
    private EType type;
    private String sendingTime;
    private Evisible visibility;
    private LocalDateTime dateObject;

    private String conversationId;

    public Message() {
        this.message = "";
        this.senderId = "";
        this.type = EType.TEXT;
        this.sendingTime = LocalDateTime.now().toString();
        this.conversationId = "";
        this.visibility = Evisible.HIDDEN;
        this.dateObject = LocalDateTime.now();
    }

    public Message(String senderId,
                   String message,
                   EType type,
                   String sendingTime,
                   Evisible visibility,
                   String conversionId) {
        this.senderId = senderId;
        this.message = message;
        this.type = type;
        this.sendingTime = sendingTime;
        this.visibility = visibility;
        this.conversationId = conversionId;
    }

    public LocalDateTime getDateObject() {
        return dateObject;
    }

    public void setDateObject(LocalDateTime dateObject) {
        this.dateObject = dateObject;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(String sendingTime) {
        this.sendingTime = sendingTime;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public EType getType() {
        return type;
    }

    public void setType(EType type) {
        this.type = type;
    }

    public Evisible getVisibility() {
        return visibility;
    }

    public void setVisibility(Evisible visibility) {
        this.visibility = visibility;
    }

    public HashMap<String, Object> convertHashMap() {
        HashMap<String, Object> messageHashMap = new HashMap<>();

        messageHashMap.put(Utils.KEY_MESSAGE, message);
        messageHashMap.put(Utils.KEY_TYPE, type);
        messageHashMap.put(Utils.KEY_SENDING_TIME, sendingTime);
        messageHashMap.put(Utils.KEY_SENDER_ID, senderId);
        messageHashMap.put(Utils.KEY_CONVERSATION_ID, conversationId);
        messageHashMap.put(Utils.KEY_IS_VISIBILITY, visibility);

        return  messageHashMap;
    }

    public void convertDocumentChangeToModel(DocumentChange documentChange) {
        senderId = documentChange.getDocument().getString(Utils.KEY_SENDER_ID);
        conversationId = documentChange.getDocument().getString(Utils.KEY_CONVERSATION_ID);
        message = documentChange.getDocument().getString(Utils.KEY_MESSAGE);
        visibility = Evisible.valueOf(documentChange.getDocument().getString(Utils.KEY_IS_VISIBILITY));
        type = EType.valueOf(documentChange.getDocument().getString(Utils.KEY_TYPE));
        sendingTime = documentChange.getDocument().getString(Utils.KEY_SENDING_TIME);
        dateObject = getLocalDateTime(sendingTime);
    }

    private LocalDateTime getLocalDateTime(String dateTimeString) {
        try {
            return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        } catch (Exception e) {
            Log.e("DateTimeConverter", "Error parsing \"" + dateTimeString + "\" : " + e.getMessage(), e);
            return null;
        }
    }

    public enum EType {
        TEXT("text"),
        IMAGE("image"),
        VIDEO("video");
        private final String name;

        EType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
