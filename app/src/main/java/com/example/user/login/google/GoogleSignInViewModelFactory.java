package com.example.user.login.google;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.user.repository.AuthRepos;
import com.example.user.login.LoginViewModelFactory;

public class GoogleSignInViewModelFactory extends LoginViewModelFactory {

    public GoogleSignInViewModelFactory(AuthRepos authRepos) {
        super(authRepos);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GoogleSignInViewModel.class)) {
            return (T) new GoogleSignInViewModel(authRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
