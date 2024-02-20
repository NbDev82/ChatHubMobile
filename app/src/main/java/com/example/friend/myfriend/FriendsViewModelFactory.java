package com.example.friend.myfriend;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.friend.service.FriendRequestService;
import com.example.user.authservice.AuthService;

public class FriendsViewModelFactory implements ViewModelProvider.Factory {

    private final AuthService authService;
    private final FriendRequestService friendRequestService;

    public FriendsViewModelFactory(AuthService authService, FriendRequestService friendRequestService) {
        this.authService = authService;
        this.friendRequestService = friendRequestService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FriendsViewModel.class)) {
            return (T) new FriendsViewModel(authService, friendRequestService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
