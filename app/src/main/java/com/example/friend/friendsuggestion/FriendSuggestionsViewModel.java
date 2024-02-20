package com.example.friend.friendsuggestion;

import com.example.friend.friendsuggestion.adapter.FriendSuggestionListener;
import com.example.friend.service.FriendRequestService;
import com.example.infrastructure.BaseViewModel;
import com.example.user.authservice.AuthService;

public class FriendSuggestionsViewModel extends BaseViewModel implements FriendSuggestionListener {

    private final AuthService authService;
    private final FriendRequestService friendRequestService;

    public FriendSuggestionsViewModel(AuthService authService, FriendRequestService friendRequestService) {
        this.authService = authService;
        this.friendRequestService = friendRequestService;
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onAddFriendClick(int position) {

    }

    @Override
    public void onRemoveClick(int position) {

    }
}
