package com.example.friend.profileviewer;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.friend.repository.FriendRequestRepos;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.UserRepos;

public class ProfileViewerViewModelFactory implements ViewModelProvider.Factory {

    private final UserRepos userRepos;
    private final AuthRepos authRepos;
    private final FriendRequestRepos friendRequestRepos;

    public ProfileViewerViewModelFactory(UserRepos userRepos,
                                         AuthRepos authRepos,
                                         FriendRequestRepos friendRequestRepos) {
        this.userRepos = userRepos;
        this.authRepos = authRepos;
        this.friendRequestRepos = friendRequestRepos;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProfileViewerViewModel.class)) {
            return (T) new ProfileViewerViewModel(userRepos, authRepos, friendRequestRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
