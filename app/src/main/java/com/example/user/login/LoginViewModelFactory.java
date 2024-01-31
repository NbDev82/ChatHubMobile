package com.example.user.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.AuthService;
import com.example.user.login.github.GithubAuthViewModel;

public class LoginViewModelFactory implements ViewModelProvider.Factory {

    protected final AuthService mAuthService;

    public LoginViewModelFactory(AuthService authService) {
        mAuthService = authService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(mAuthService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
