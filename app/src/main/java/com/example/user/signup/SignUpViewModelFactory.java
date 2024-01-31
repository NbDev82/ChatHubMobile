package com.example.user.signup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.AuthService;

public class SignUpViewModelFactory implements ViewModelProvider.Factory {

    private final AuthService mAuthService;

    public SignUpViewModelFactory(AuthService authService) {
        this.mAuthService = authService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SignUpViewModel.class)) {
            return (T) new SignUpViewModel(mAuthService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
