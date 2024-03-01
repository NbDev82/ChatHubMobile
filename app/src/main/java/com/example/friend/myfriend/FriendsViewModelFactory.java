package com.example.friend.myfriend;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.friend.repository.FriendRequestRepos;
import com.example.user.repository.AuthRepos;

public class FriendsViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepos authRepos;
    private final FriendRequestRepos friendRequestRepos;

    public FriendsViewModelFactory(AuthRepos authRepos, FriendRequestRepos friendRequestRepos) {
        this.authRepos = authRepos;
        this.friendRequestRepos = friendRequestRepos;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FriendsViewModel.class)) {
            return (T) new FriendsViewModel(authRepos, friendRequestRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
