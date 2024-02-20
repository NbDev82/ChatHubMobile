package com.example.user.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.authservice.AuthService;

public class UserProfileViewModelFactory implements ViewModelProvider.Factory {

    private final AuthService authService;

    public UserProfileViewModelFactory(AuthService authService) {
        this.authService = authService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserProfileViewModel.class)) {
            return (T) new UserProfileViewModel(authService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
