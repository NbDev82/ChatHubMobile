package com.example.chat.message;

import com.example.chat.Utils;
import com.example.chat.enums.Evisible;

import java.time.LocalDateTime;
import java.util.HashMap;

import javax.annotation.Nullable;

public class Message {
    private String senderId, receiverId, message;
    private String type;
    private LocalDateTime sendingTime;
    private Evisible visibility;

    private String conversationId, conversationName, conversationImage;

    public Message() {
        this.message = "";
        this.senderId = "";
        this.receiverId = "";
        this.type = "";
        this.sendingTime = LocalDateTime.now();
        this.conversationId = "";
        this.conversationName = "";
        this.conversationImage = "";
        this.visibility = Evisible.HIDDEN;
    }

    public Message(String senderId,
                   String receiverId,
                   String message,
                   String type,
                   LocalDateTime sendingTime,
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

    public LocalDateTime getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(LocalDateTime sendingTime) {
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
}
