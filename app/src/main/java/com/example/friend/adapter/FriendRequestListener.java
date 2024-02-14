package com.example.friend.adapter;

public interface FriendRequestListener {
    void onItemClick(FriendRequestView request);
    void onAcceptClick(FriendRequestView request);
    void onRejectClick(FriendRequestView request);
}
