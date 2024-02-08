package com.example.user.login.otp.verify;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.AuthService;

public class VerifyOtpViewModelFactory implements ViewModelProvider.Factory {

    private final AuthService authService;

    public VerifyOtpViewModelFactory(AuthService authService) {
        this.authService = authService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(VerifyOtpViewModel.class)) {
            return (T) new VerifyOtpViewModel(authService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
