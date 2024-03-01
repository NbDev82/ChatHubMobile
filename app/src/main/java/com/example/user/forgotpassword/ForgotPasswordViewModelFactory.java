package com.example.user.forgotpassword;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.repository.AuthRepos;

public class ForgotPasswordViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepos authRepos;

    public ForgotPasswordViewModelFactory(AuthRepos mAuthService) {
        this.authRepos = mAuthService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ForgotPasswordViewModel.class)) {
            return (T) new ForgotPasswordViewModel(authRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
