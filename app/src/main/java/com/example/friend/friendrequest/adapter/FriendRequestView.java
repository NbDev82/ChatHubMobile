package com.example.friend.friendrequest.adapter;

import android.graphics.Bitmap;

import com.example.friend.FriendRequest;

public class FriendRequestView {
    private String id;
    private Bitmap recipientAvatar;
    private String recipientName;
    private int mutualFriends;
    private String timeAgo;
    private FriendRequest.EStatus status;

    public FriendRequestView() {
    }

    public FriendRequestView(String id, Bitmap recipientAvatar, String recipientName,
                             int mutualFriends, String timeAgo, FriendRequest.EStatus status) {
        this.id = id;
        this.recipientAvatar = recipientAvatar;
        this.recipientName = recipientName;
        this.mutualFriends = mutualFriends;
        this.timeAgo = timeAgo;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bitmap getRecipientAvatar() {
        return recipientAvatar;
    }

    public void setRecipientAvatar(Bitmap recipientAvatar) {
        this.recipientAvatar = recipientAvatar;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
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
}
