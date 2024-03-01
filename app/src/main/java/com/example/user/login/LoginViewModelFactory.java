package com.example.user.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.repository.AuthRepos;

public class LoginViewModelFactory implements ViewModelProvider.Factory {

    protected final AuthRepos authRepos;

    public LoginViewModelFactory(AuthRepos authRepos) {
        this.authRepos = authRepos;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(authRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
