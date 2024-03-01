package com.example.friend.friendsuggestion;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.friend.repository.FriendRequestRepos;
import com.example.user.repository.AuthRepos;

public class FriendSuggestionsViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepos authRepos;
    private final FriendRequestRepos friendRequestRepos;

    public FriendSuggestionsViewModelFactory(AuthRepos authRepos, FriendRequestRepos friendRequestRepos) {
        this.authRepos = authRepos;
        this.friendRequestRepos = friendRequestRepos;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FriendSuggestionsViewModel.class)) {
            return (T) new FriendSuggestionsViewModel(authRepos, friendRequestRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
