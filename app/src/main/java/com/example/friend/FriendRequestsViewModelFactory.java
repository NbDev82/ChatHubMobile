package com.example.friend;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FriendRequestsViewModelFactory implements ViewModelProvider.Factory {

    public FriendRequestsViewModelFactory() {
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FriendRequestsViewModel.class)) {
            return (T) new FriendRequestsViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
