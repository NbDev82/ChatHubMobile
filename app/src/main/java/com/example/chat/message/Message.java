package com.example.chat.message;

import android.util.Log;

import com.example.chat.Utils;
import com.example.chat.enums.Evisible;
import com.google.firebase.firestore.DocumentChange;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


public class Message {
    private String senderId, receiverId, message;
    private String type;
    private String sendingTime;
    private Evisible visibility;
    private LocalDateTime dateObject;

    private String conversationId, conversationName, conversationImage;

    public Message() {
        this.message = "";
        this.senderId = "";
        this.receiverId = "";
        this.type = "";
        this.sendingTime = LocalDateTime.now().toString();
        this.conversationId = "";
        this.conversationName = "";
        this.conversationImage = "";
        this.visibility = Evisible.HIDDEN;
        this.dateObject = LocalDateTime.now();
    }

    public Message(String senderId,
                   String receiverId,
                   String message,
                   String type,
                   String sendingTime,
                   Evisible visibility,
                   String conversionId,
                   String conversationName,
                   String conversationImage) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.type = type;
        this.sendingTime = sendingTime;
        this.visibility = visibility;
        this.conversationId = conversionId;
        this.conversationName = conversationName;
        this.conversationImage = conversationImage;
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

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
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

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public String getConversationImage() {
        return conversationImage;
    }

    public void setConversationImage(String conversationImage) {
        this.conversationImage = conversationImage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
        type = documentChange.getDocument().getString(Utils.KEY_TYPE);
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
}
