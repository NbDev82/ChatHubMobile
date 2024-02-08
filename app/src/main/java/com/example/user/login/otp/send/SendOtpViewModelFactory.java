package com.example.user.login.otp.send;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.AuthService;

public class SendOtpViewModelFactory implements ViewModelProvider.Factory {

    private final AuthService authService;

    public SendOtpViewModelFactory(AuthService authService) {
        this.authService = authService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SendOtpViewModel.class)) {
            return (T) new SendOtpViewModel(authService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
