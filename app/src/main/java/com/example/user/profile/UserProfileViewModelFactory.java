package com.example.user.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.repository.AuthRepos;
import com.example.user.repository.UserRepos;

public class UserProfileViewModelFactory implements ViewModelProvider.Factory {

    private final UserRepos userRepos;
    private final AuthRepos authRepos;

    public UserProfileViewModelFactory(UserRepos userRepos, AuthRepos authRepos) {
        this.userRepos = userRepos;
        this.authRepos = authRepos;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserProfileViewModel.class)) {
            return (T) new UserProfileViewModel(userRepos, authRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
