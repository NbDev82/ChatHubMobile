package com.example.user.login.github;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.authservice.AuthService;

public class GithubAuthViewModelFactory implements ViewModelProvider.Factory {

    private final Activity activity;
    private final AuthService authService;

    public GithubAuthViewModelFactory(Activity activity, AuthService authService) {
        this.activity = activity;
        this.authService = authService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GithubAuthViewModel.class)) {
            return (T) new GithubAuthViewModel(activity, authService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
