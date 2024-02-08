package com.example.user.changepassword;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.AuthService;

public class ChangePasswordViewModelFactory implements ViewModelProvider.Factory {

    private final AuthService authService;

    public ChangePasswordViewModelFactory(AuthService mAuthService) {
        this.authService = mAuthService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ChangePasswordViewModel.class)) {
            return (T) new ChangePasswordViewModel(authService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
