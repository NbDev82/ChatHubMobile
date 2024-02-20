package com.example.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.authservice.AuthService;

public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private final AuthService authService;

    public HomeViewModelFactory(AuthService authService) {
        this.authService = authService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(authService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
