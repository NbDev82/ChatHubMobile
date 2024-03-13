package com.example.friend.friendsuggestion.adapter;

public interface FriendSuggestionListener {
    void onItemClick(int position);

    void onAddFriendClick(int position);

    void onRemoveClick(int position);
}