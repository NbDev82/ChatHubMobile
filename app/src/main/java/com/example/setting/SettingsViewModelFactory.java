package com.example.setting;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.AuthService;

public class SettingsViewModelFactory implements ViewModelProvider.Factory {

    private AuthService mAuthService;

    public SettingsViewModelFactory(AuthService authService) {
        mAuthService = authService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            return (T) new SettingsViewModel(mAuthService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
