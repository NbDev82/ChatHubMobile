package com.example.user.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.AuthService;

public class LoginViewModelFactory implements ViewModelProvider.Factory {

    protected final AuthService authService;

    public LoginViewModelFactory(AuthService authService) {
        this.authService = authService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(authService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
