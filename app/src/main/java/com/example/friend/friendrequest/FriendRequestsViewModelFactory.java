package com.example.friend.friendrequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.friend.service.FriendRequestService;
import com.example.user.AuthService;

public class FriendRequestsViewModelFactory implements ViewModelProvider.Factory {

    private final AuthService authService;
    private final FriendRequestService friendRequestService;

    public FriendRequestsViewModelFactory(AuthService authService, FriendRequestService friendRequestService) {
        this.authService = authService;
        this.friendRequestService = friendRequestService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FriendRequestsViewModel.class)) {
            return (T) new FriendRequestsViewModel(authService, friendRequestService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
