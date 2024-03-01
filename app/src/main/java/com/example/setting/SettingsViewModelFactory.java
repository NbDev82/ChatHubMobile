package com.example.setting;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.repository.AuthRepos;

public class SettingsViewModelFactory implements ViewModelProvider.Factory {

    private AuthRepos authRepos;

    public SettingsViewModelFactory(AuthRepos authRepos) {
        this.authRepos = authRepos;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            return (T) new SettingsViewModel(authRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
