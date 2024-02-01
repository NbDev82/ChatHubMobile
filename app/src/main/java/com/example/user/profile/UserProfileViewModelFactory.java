package com.example.user.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.AuthService;

public class UserProfileViewModelFactory implements ViewModelProvider.Factory {

    private final AuthService mAuthService;

    public UserProfileViewModelFactory(AuthService authService) {
        mAuthService = authService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserProfileViewModel.class)) {
            return (T) new UserProfileViewModel(mAuthService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
