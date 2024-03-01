package com.example.friend;

import java.util.Date;

public class FriendRequest {
    private String id;
    private String senderId;
    private String recipientId;
    private EStatus status;
    private Date createdTime;

    public FriendRequest() {
    }

    public FriendRequest(String id, String senderId, String recipientId,
                         EStatus status, Date createdTime) {
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.status = status;
        this.createdTime = createdTime;
    }

    public FriendRequest(String senderId, String recipientId, EStatus status, Date createdTime) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.status = status;
        this.createdTime = createdTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public enum EStatus {
        ACCEPTED, REJECTED, PENDING, NONE
    }
}
