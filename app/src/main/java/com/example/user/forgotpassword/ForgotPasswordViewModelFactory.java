package com.example.user.forgotpassword;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.home.HomeViewModel;
import com.example.user.AuthService;

public class ForgotPasswordViewModelFactory implements ViewModelProvider.Factory {

    private final AuthService mAuthService;

    public ForgotPasswordViewModelFactory(AuthService mAuthService) {
        this.mAuthService = mAuthService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ForgotPasswordViewModel.class)) {
            return (T) new ForgotPasswordViewModel(mAuthService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
