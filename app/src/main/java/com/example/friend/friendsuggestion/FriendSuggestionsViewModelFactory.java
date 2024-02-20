package com.example.friend.friendsuggestion;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.friend.service.FriendRequestService;
import com.example.user.authservice.AuthService;

public class FriendSuggestionsViewModelFactory implements ViewModelProvider.Factory {

    private final AuthService authService;
    private final FriendRequestService friendRequestService;

    public FriendSuggestionsViewModelFactory(AuthService authService, FriendRequestService friendRequestService) {
        this.authService = authService;
        this.friendRequestService = friendRequestService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FriendSuggestionsViewModel.class)) {
            return (T) new FriendSuggestionsViewModel(authService, friendRequestService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
