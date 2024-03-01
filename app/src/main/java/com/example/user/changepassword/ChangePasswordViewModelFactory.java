package com.example.user.changepassword;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.repository.AuthRepos;

public class ChangePasswordViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepos authRepos;

    public ChangePasswordViewModelFactory(AuthRepos mAuthService) {
        this.authRepos = mAuthService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ChangePasswordViewModel.class)) {
            return (T) new ChangePasswordViewModel(authRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
