package com.example.user.emaildetails;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.authservice.AuthService;

public class EmailDetailsViewModelFactory implements ViewModelProvider.Factory {

    private final AuthService authService;

    public EmailDetailsViewModelFactory(AuthService mAuthService) {
        this.authService = mAuthService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EmailDetailsViewModel.class)) {
            return (T) new EmailDetailsViewModel(authService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
