package com.example.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.repository.AuthRepos;

public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepos authRepos;

    public HomeViewModelFactory(AuthRepos authRepos) {
        this.authRepos = authRepos;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(authRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
