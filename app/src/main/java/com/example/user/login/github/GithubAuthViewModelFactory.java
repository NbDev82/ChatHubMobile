package com.example.user.login.github;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.AuthService;

public class GithubAuthViewModelFactory implements ViewModelProvider.Factory {

    private final Activity mActivity;
    private final AuthService mAuthService;

    public GithubAuthViewModelFactory(Activity activity, AuthService authService) {
        this.mActivity = activity;
        this.mAuthService = authService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GithubAuthViewModel.class)) {
            return (T) new GithubAuthViewModel(mActivity, mAuthService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
