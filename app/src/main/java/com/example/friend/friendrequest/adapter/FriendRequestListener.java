package com.example.friend.friendrequest.adapter;

public interface FriendRequestListener {
    void onItemClick(int position);

    void onAcceptClick(int position);

    void onRejectClick(int position);
}
