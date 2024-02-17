package com.example.friend.friendrequest.adapter;

import android.graphics.Bitmap;

import com.example.friend.FriendRequest;

public class FriendRequestView {
    private String id;
    private String senderId;
    private Bitmap senderAvatar;
    private String senderName;
    private int mutualFriends;
    private String timeAgo;
    private FriendRequest.EStatus status;
    private boolean isLoading;

    public FriendRequestView() {
    }

    public FriendRequestView(String id, String senderId, Bitmap senderAvatar,
                             String senderName, int mutualFriends, String timeAgo,
                             FriendRequest.EStatus status, boolean isLoading) {
        this.id = id;
        this.senderId = senderId;
        this.senderAvatar = senderAvatar;
        this.senderName = senderName;
        this.mutualFriends = mutualFriends;
        this.timeAgo = timeAgo;
        this.status = status;
        this.isLoading = isLoading;
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

    public Bitmap getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(Bitmap senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getMutualFriends() {
        return mutualFriends;
    }

    public void setMutualFriends(int mutualFriends) {
        this.mutualFriends = mutualFriends;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public FriendRequest.EStatus getStatus() {
        return status;
    }

    public void setStatus(FriendRequest.EStatus status) {
        this.status = status;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
