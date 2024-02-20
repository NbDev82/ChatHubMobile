package com.example.user.forgotpassword;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.authservice.AuthService;

public class ForgotPasswordViewModelFactory implements ViewModelProvider.Factory {

    private final AuthService authService;

    public ForgotPasswordViewModelFactory(AuthService mAuthService) {
        this.authService = mAuthService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ForgotPasswordViewModel.class)) {
            return (T) new ForgotPasswordViewModel(authService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
