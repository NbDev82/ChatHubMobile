package com.example.user.login.github;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.repository.AuthRepos;

public class GithubAuthViewModelFactory implements ViewModelProvider.Factory {

    private final Activity activity;
    private final AuthRepos authRepos;

    public GithubAuthViewModelFactory(Activity activity, AuthRepos authRepos) {
        this.activity = activity;
        this.authRepos = authRepos;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GithubAuthViewModel.class)) {
            return (T) new GithubAuthViewModel(activity, authRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
