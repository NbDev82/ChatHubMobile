package com.example.friend;

public enum EFriendRequestField {
    COLLECTION_NAME("friend_requests"),
    SENDER_ID("senderId"),
    RECIPIENT_ID("recipientId"),
    STATUS("status"),
    CREATED_TIME("createdTime");

    private String name;

    EFriendRequestField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
