package com.example.friend.profileviewer;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.friend.service.FriendRequestService;
import com.example.user.authservice.AuthService;

public class ProfileViewerViewModelFactory implements ViewModelProvider.Factory {

    private final AuthService authService;
    private final FriendRequestService friendRequestService;

    public ProfileViewerViewModelFactory(AuthService authService,
                                         FriendRequestService friendRequestService) {
        this.authService = authService;
        this.friendRequestService = friendRequestService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProfileViewerViewModel.class)) {
            return (T) new ProfileViewerViewModel(authService, friendRequestService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
